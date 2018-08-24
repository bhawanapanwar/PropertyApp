package com.kotlin.testapplication.Activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kotlin.testapplication.Adapters.PropertyAdapter;
import com.kotlin.testapplication.Model.Person;
import com.kotlin.testapplication.Model.PremiumUpdate;
import com.kotlin.testapplication.Model.StandardUpdate;
import com.kotlin.testapplication.Model.Update;
import com.kotlin.testapplication.Network.ApiServices;
import com.kotlin.testapplication.Network.RetrofitInstance;
import com.kotlin.testapplication.R;
import com.kotlin.testapplication.Utils.ConnectionDetector;
import com.kotlin.testapplication.Utils.ProgressBarHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PropertyActivity extends BaseActivity {

    private static final String TAG = "FragmentLayoutACTIVITY";
    private ApiServices apiServices;
    private static PropertyAdapter mPropertyAdapter;
    private static RecyclerView mRview;
    public static boolean lock;
    private static List<Update> mPropertyArry;
    private ProgressBarHandler mProgressBarHandler;
    private ConnectionDetector mIsInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, " onCreate()");

       /* Toast.makeText(this, "PropertyActivity: OnCreate()", Toast.LENGTH_SHORT)
                .show();
*/
        // Sets the view. Depending on orientation it will select either
        // res/layout/fragment_layout.xml (portrait mode) or
        // res/layout-land/fragment_layout.xml (landscape mode). This is done
        // automatically by the system.
        setContentView(R.layout.activity_fragment_layout);

        if(lock==false) {
            mPropertyArry=new ArrayList<>();
            mIsInternet = new ConnectionDetector(PropertyActivity.this);
            if (mIsInternet.isConnectingToInternet() == true) {
                hitApi();
            } else {
                Toast.makeText(PropertyActivity.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }

        }
    }

// This is a secondary activity, to show what the user has selected when the
    // screen is not large enough to show it all in one activity.

    public static class DetailsActivity extends Activity {

        private static final String TAG = "DetailsACTIVITY";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Log.d(TAG, " onCreate()");

          //  Toast.makeText(this, "DetailsActivity", Toast.LENGTH_SHORT).show();

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // If the screen is now in landscape mode, we can show the
                // dialog in-line with the list so we don't need this activity.
                finish();
                return;
            }

            if (savedInstanceState == null) {
                // During initial setup, plug in the details fragment.

                // create fragment
                DetailsFragment details = new DetailsFragment();

                // get and set the position input by user (i.e., "index")
                // which is the construction arguments for this fragment
                details.setArguments(getIntent().getExtras());

                //
                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, details).commit();
            }
        }

    }

    // Displays a list of items that are managed by an adapter

    public static class PropertyListFragment extends Fragment implements PropertyAdapter.CustomItemClickListener{

        public static String fragmentname;

        private static final String TAG = "PropertyListFragment";
        boolean mDualPane;
        int mCurCheckPosition = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_property, container,
                    false);
            fragmentname = "FeedFragment";

            mRview = (RecyclerView) view.findViewById(R.id.recyclerView);

            InitViews();


            return view;
        }

        public void InitViews() {

            final LinearLayoutManager Layoutmanager = new LinearLayoutManager(getActivity()) {
                @Override
                protected int getExtraLayoutSpace(RecyclerView.State state) {
                    return 300;
                }
            };

            mRview.setLayoutManager(Layoutmanager);

        }

        // onActivityCreated() is called when the activity's onCreate() method
        // has returned.

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Log.d(TAG, "onActivityCreated()");

            mPropertyAdapter = new PropertyAdapter(getActivity(), mPropertyArry,this);
            mRview.setAdapter(mPropertyAdapter);

            // R.id.details relates to the res/layout-land/fragment_layout.xml
            // This is first created when the phone is switched to landscape
            // mode

            View detailsFrame = getActivity().findViewById(R.id.details);

          //  Toast.makeText(getActivity(), "detailsFrame " + detailsFrame,
            //        Toast.LENGTH_LONG).show();


            mDualPane = detailsFrame != null
                    && detailsFrame.getVisibility() == View.VISIBLE;

         //   Toast.makeText(getActivity(), "mDualPane " + mDualPane,
         //           Toast.LENGTH_LONG).show();

            if (savedInstanceState != null) {
                // Restore last state for checked position.
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }

            if (mDualPane) {
                // In dual-pane mode, the list view highlights the selected
                // item.
                showDetails(mCurCheckPosition);
            } else {
                //  highlight in uni-pane

            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
           /* Toast.makeText(getActivity(), "onSaveInstanceState",
                    Toast.LENGTH_LONG).show();*/

            lock=true;

            outState.putInt("curChoice", mCurCheckPosition);
        }

        // Helper function to show the details of a selected item, either by
        // displaying a fragment in-place in the current UI, or starting a whole
        // new activity in which it is displayed.

        void showDetails(int index) {
            mCurCheckPosition = index;

            // The basic design is mutli-pane (landscape on the phone) allows us
            // to display both fragments (titles and details) with in the same
            // activity; that is PropertyActivity -- one activity with two
            // fragments.
            // Else, it's single-pane (portrait on the phone) and we fire
            // another activity to render the details fragment - two activities
            // each with its own fragment .
            //
            if (mDualPane) {
                // We can display everything in-place with fragments
                // Check what fragment is currently shown, replace if needed.
                DetailsFragment details = (DetailsFragment) getFragmentManager()
                        .findFragmentById(R.id.details);
                if (details == null || details.getShownIndex() != index) {
                    // Make new fragment to show this selection.

                    details = DetailsFragment.newInstance(index);


                    // Execute a transaction, replacing any existing fragment
                    // with this one inside the frame.

                    FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.details, details);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }

            } else {
                // Otherwise we need to launch a new activity to display
                // the dialog fragment with selected text.
                // That is: if this is a single-pane (e.g., portrait mode on a
                // phone) then fire DetailsActivity to display the details
                // fragment

                // Create an intent for starting the DetailsActivity
                Intent intent = new Intent();

                // explicitly set the activity context and class
                // associated with the intent (context, class)
                intent.setClass(getActivity(), DetailsActivity.class);

                // pass the current position
                intent.putExtra("index", index);

                startActivity(intent);
            }
        }

        @Override
        public void onItemClick(View v, int position) {
          /*  Toast.makeText(getActivity(),
                    "onListItemClick position is" + position, Toast.LENGTH_SHORT)
                    .show();*/

            showDetails(position);
        }
    }

    // This is the secondary fragment, displaying the details of a particular
    // item.

    public static class DetailsFragment extends Fragment {

        private static final String TAG = "DetailsFRAGMENT";
        // Create a new instance of DetailsFragment, initialized to show the
        // text at 'index'.

        public static DetailsFragment newInstance(int index) {
            DetailsFragment f = new DetailsFragment();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", index);
            f.setArguments(args);

            return f;
        }


        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // programmatically create a scrollview and texview for the text in
            // the container/fragment layout. Set up the properties and add the
            // view.


            ScrollView scroller = new ScrollView(getActivity());
            TextView text = new TextView(getActivity());
            int padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 28, getActivity()
                            .getResources().getDisplayMetrics());
            text.setPadding(padding, padding, padding, padding);
            text.setTextColor(Color.BLUE);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
            scroller.addView(text);

            ObjectAnimator colorAnim = ObjectAnimator.ofInt(text, "textColor",
                    Color.RED, Color.BLUE);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setDuration(3000);
            colorAnim.start();

            text.setText(mPropertyArry.get(getShownIndex()).getUpdateUser().getId());
            return scroller;
        }
    }

    void hitApi() {
        lockScreenOrientation();
        apiServices = RetrofitInstance.INSTANCE.getRetrofitInstance().create(ApiServices.class);
        mProgressBarHandler = new ProgressBarHandler(PropertyActivity.this);
        mProgressBarHandler.show();

        Call<JsonElement> call = apiServices.property();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                String result = response.body().toString();
                try {
                    Log.e("result1",result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray listings = data.getJSONArray("listings");

                    mPropertyArry.clear();

                    for (int i = 0; i < listings.length(); i++) {
                        JSONObject jobj = listings.getJSONObject(i);
                        Person prop=new Person();

                        prop.setId(jobj.getString("Id"));
                        prop.setArea(jobj.getString("Area"));
                        prop.setBathrooms(jobj.getString("Bathrooms"));
                        prop.setBedrooms(jobj.getString("Bedrooms"));
                        prop.setCarspaces(jobj.getString("Carspaces"));
                        prop.setDescription(jobj.getString("Description"));
                        prop.setDisplayPrice(jobj.getString("DisplayPrice"));

                        JSONObject owner = jobj.getJSONObject("owner");
                        prop.setName(owner.getString("name"));
                        prop.setLastName(owner.getString("lastName"));
                        JSONObject image = owner.getJSONObject("image");
                        JSONObject medium = image.getJSONObject("medium");
                        prop.setUrl(medium.getString("url"));

                        JSONArray ImageUrls = jobj.getJSONArray("ImageUrls");
                        prop.setImgUrl(ImageUrls.get(2).toString());
                        prop.setImgUrlTwo(ImageUrls.get(3).toString());


                        JSONObject Location = jobj.getJSONObject("Location");
                        prop.setAddress(Location.getString("Address"));
                        prop.setAddress2(Location.getString("Address2"));
                        prop.setState(Location.getString("State"));
                        prop.setSuburb(Location.getString("Suburb"));

                        prop.setPremium(jobj.getString("is_premium"));
                        if(jobj.getString("is_premium").equals("0")){
                            StandardUpdate rv=new StandardUpdate(prop);
                            mPropertyArry.add(rv);
                        }else{
                            PremiumUpdate up=new PremiumUpdate(prop);
                            mPropertyArry.add(up);
                        }

                    }

                    mPropertyAdapter.notifyDataSetChanged();

                    mProgressBarHandler.hide();
                    unlockScreenOrientation();


                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressBarHandler.hide();
                    unlockScreenOrientation();

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("fail","");
                mProgressBarHandler.hide();
                unlockScreenOrientation();
            }
        });
    }

    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


}
