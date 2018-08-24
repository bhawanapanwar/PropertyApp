package com.kotlin.testapplication.Utils

import android.content.Context
import android.content.SharedPreferences

class AppSharedPrefrence(context: Context) {

    var appSharedPrefs: SharedPreferences? = null
    var prefsEditor: SharedPreferences.Editor? = null
        private set

    var lockOnce: Boolean?
        get() = appSharedPrefs!!.getBoolean("lockOnce", false)
        set(lockOnce) {
            this.prefsEditor = appSharedPrefs!!.edit()
            prefsEditor!!.putBoolean("lockOnce", lockOnce!!)
            prefsEditor!!.commit()
        }

    var lock: String?
        get() = appSharedPrefs!!.getString("lock", null)
        set(lock) {
            this.prefsEditor = appSharedPrefs!!.edit()
            prefsEditor!!.putString("lock", lock)
            prefsEditor!!.commit()
        }

    init {
        this.appSharedPrefs = context.getSharedPreferences("sharedprefproperty", Context.MODE_PRIVATE)
        this.prefsEditor = appSharedPrefs!!.edit()
    }

    fun Commit() {
        prefsEditor!!.commit()
    }

    fun clearallSharedPrefernce() {
        prefsEditor!!.clear()
        prefsEditor!!.commit()
    }

    companion object {
        private var appSharedPrefrence: AppSharedPrefrence? = null

        fun getsharedprefInstance(con: Context): AppSharedPrefrence {
            if (appSharedPrefrence == null)
                appSharedPrefrence = AppSharedPrefrence(con)
            return appSharedPrefrence!!
        }
    }


}
