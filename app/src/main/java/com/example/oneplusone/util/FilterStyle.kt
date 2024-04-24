package com.example.oneplusone.util

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.oneplusone.R

class FilterStyle {

    fun resetPreviousFilterStyle(context: Context,selectMainFilter: View?) {
        selectMainFilter?.let {
            it.alpha = 1f
            it.backgroundTintList = null
            it.findViewById<TextView>(R.id.main_filter_text).setTextColor(
                ContextCompat.getColor(context, R.color.black))
        }
    }

    fun updateFilterStyle(context: Context,selectMainFilter: View?) {
        selectMainFilter?.let {
            it.alpha = 0.5f
            it.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.filter_background_selected)
            )
            it.findViewById<TextView>(R.id.main_filter_text).setTextColor(
                ContextCompat.getColor(context, R.color.filter_text_selected))
        }
    }
}