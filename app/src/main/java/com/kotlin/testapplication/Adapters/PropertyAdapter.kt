package com.kotlin.testapplication.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kotlin.testapplication.Model.PremiumUpdate
import com.kotlin.testapplication.R
import com.kotlin.testapplication.Model.StandardUpdate
import com.kotlin.testapplication.Model.Update
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide


class PropertyAdapter(val context: Context,
                      var updatesList: List<Update>,var listener :CustomItemClickListener )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PREMIUM = 1
        const val TYPE_STANDARD = 0
    }

    override fun getItemViewType(position: Int): Int {
        val type = when (updatesList[position].updateType) {
            Update.TYPE.STANDARD -> TYPE_STANDARD
        // other types...
            else -> TYPE_PREMIUM
        }
        return type
    }

    /*
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //p0?.bindItem(updatesList[position])
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder?=null
        val view: View
        when (viewType) {
            TYPE_PREMIUM -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_premium, parent, false)
                return PremiumViewHolder(view)
            }
            TYPE_STANDARD -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_standard, parent, false)
                return StandardViewHolder(view)
            }
        }

        return viewHolder!!
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        (p0 as UpdateViewHolder).bindViews(updatesList[position])
    }

    override fun getItemCount(): Int {
        return updatesList.size

    }

    fun setUpdates(updates: List<Update>) {
        updatesList = updates
        notifyDataSetChanged()
    }


    inner class StandardViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), UpdateViewHolder {

        // get the views reference from itemView...
        var textName:TextView= itemView.findViewById(R.id.textName) as TextView
        var textTitle:TextView= itemView.findViewById(R.id.textTitle) as TextView
        var textAddress:TextView= itemView.findViewById(R.id.textAddress) as TextView
        var textBedroom:TextView= itemView.findViewById(R.id.textBedroom) as TextView
        var textBathRoom:TextView= itemView.findViewById(R.id.textBathRoom) as TextView
        var textCar:TextView= itemView.findViewById(R.id.textCarNum) as TextView
        var textArea:TextView= itemView.findViewById(R.id.textArea) as TextView
        var textPrice:TextView= itemView.findViewById(R.id.textPrice) as TextView
        var imgProfile:ImageView=itemView.findViewById(R.id.imgProfile) as ImageView
        var imgFullImg:ImageView=itemView.findViewById(R.id.imgFullImg) as ImageView



        override fun bindViews(update: Update) {
            val mUpdate = update as StandardUpdate

            // bind update values to views
            textName.text=mUpdate.updateUser.name+" "+mUpdate.updateUser.lastName
            textTitle.text=mUpdate.updateUser.description
            textAddress.text=mUpdate.updateUser.Address
            textBedroom.text=mUpdate.updateUser.bedrooms
            textBathRoom.text=mUpdate.updateUser.bathrooms
            textCar.text=mUpdate.updateUser.carspaces
            textArea.text=mUpdate.updateUser.area
            textPrice.text="$"+mUpdate.updateUser.displayPrice

            Glide.with(context).load(mUpdate.updateUser.url).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.color.material_grey_100)).into(imgProfile)

            Glide.with(context).load(mUpdate.updateUser.imgUrl).into(imgFullImg)


            itemView.setOnClickListener{

                listener.onItemClick(it,adapterPosition)
            }

        }
    }

    inner class PremiumViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), UpdateViewHolder {

        // get the views reference from itemView...
        var textName:TextView= itemView.findViewById(R.id.textName) as TextView
        var textTitle:TextView= itemView.findViewById(R.id.textTitle) as TextView
        var textAddress:TextView= itemView.findViewById(R.id.textAddress) as TextView
        var textBedroom:TextView= itemView.findViewById(R.id.textBedroom) as TextView
        var textBathRoom:TextView= itemView.findViewById(R.id.textBathRoom) as TextView
        var textCar:TextView= itemView.findViewById(R.id.textCarNum) as TextView
        var textArea:TextView= itemView.findViewById(R.id.textArea) as TextView
        var textPrice:TextView= itemView.findViewById(R.id.textPrice) as TextView
        var imgProfile:ImageView=itemView.findViewById(R.id.imgProfile) as ImageView
        var imgFullImg:ImageView=itemView.findViewById(R.id.imgFullImg) as ImageView


        override fun bindViews(update: Update) {
            val mUpdate = update as PremiumUpdate

            // bind update values to views
            textName.text=mUpdate.updateUser.name+" "+mUpdate.updateUser.lastName
            textTitle.text=mUpdate.updateUser.description
            textAddress.text=mUpdate.updateUser.Address
            textBedroom.text=mUpdate.updateUser.bedrooms
            textBathRoom.text=mUpdate.updateUser.bathrooms
            textCar.text=mUpdate.updateUser.carspaces
            textArea.text=mUpdate.updateUser.area
            textPrice.text="$"+mUpdate.updateUser.displayPrice

            Glide.with(context).load(mUpdate.updateUser.url).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.color.material_grey_100)).into(imgProfile)

            Glide.with(context).load(mUpdate.updateUser.imgUrlTwo).into(imgFullImg)


            itemView.setOnClickListener{

                listener.onItemClick(it,adapterPosition)
            }


        }
    }

    interface UpdateViewHolder {
        fun bindViews(update: Update)
    }

    interface CustomItemClickListener {
        fun onItemClick(v: View, position: Int)
    }


}
