package com.kotlin.testapplication.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.kotlin.testapplication.Utils.AppSharedPrefrence


abstract class BaseActivity : AppCompatActivity() {
    companion object {
        var appSharedPreference: AppSharedPrefrence?=null
        var cxt: Activity?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        cxt = this@BaseActivity
        appSharedPreference = AppSharedPrefrence.getsharedprefInstance(cxt!!)

    }

    fun GotoNextActivity(currentActivity: Activity, nextActivity: Class<*>) {
        val intent = Intent(currentActivity, nextActivity)
        startActivity(intent)
    }

    fun GotoNextActivityClearTop(currentActivity: Activity, nextActivity: Class<*>) {
        val intent = Intent(currentActivity, nextActivity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
