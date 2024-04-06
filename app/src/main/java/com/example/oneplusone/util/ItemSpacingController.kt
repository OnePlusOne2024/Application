package com.example.oneplusone.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingController(private val divStart:Int,private val divEnd:Int,private val divHeight:Int) : RecyclerView.ItemDecoration() {

    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.left = divStart
        outRect.right = divEnd
        outRect.top=divHeight


    }
}
