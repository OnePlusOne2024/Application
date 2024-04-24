package com.example.oneplusone.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView

class FilterAnimated {

    fun viewAnimated(isVisible:Boolean,filterBarDetail:ImageView){
        if (isVisible) {
            filterBarDetail.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(50)
                    .setListener(null)
            }
        } else {
            filterBarDetail.animate()
                .alpha(0f)
                .setDuration(50)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        filterBarDetail.visibility = View.GONE
                    }
                })
        }
    }

}