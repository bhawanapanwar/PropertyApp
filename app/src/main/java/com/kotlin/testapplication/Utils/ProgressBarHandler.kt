package com.kotlin.testapplication.Utils

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout

class ProgressBarHandler(private val mContext: Activity) {
    private val mProgressBar: ProgressBar

    init {
        val layout = mContext.findViewById<View>(android.R.id.content).rootView as ViewGroup
        mProgressBar = ProgressBar(mContext, null, android.R.attr.progressBarStyleLarge)
        mProgressBar.isIndeterminate = false
        mProgressBar.isClickable = false
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        val rl = RelativeLayout(mContext)
        rl.gravity = Gravity.CENTER
        rl.addView(mProgressBar)
        layout.addView(rl, params)
        hide()
    }

    fun show() {
        mProgressBar.visibility = View.VISIBLE

        mContext.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun setcolor() {
        mProgressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
    }


    fun hide() {
        mProgressBar.visibility = View.GONE
        mContext.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}