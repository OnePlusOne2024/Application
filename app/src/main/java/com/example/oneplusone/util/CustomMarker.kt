package com.example.oneplusone.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.oneplusone.R
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.enums.ConvenienceType

class CustomMarker {

    @SuppressLint("InflateParams")
    fun createDrawableFromView(
        context: Context,
        convenienceData: ConvenienceData,
        selectedMarker: Boolean

    ): Bitmap {

        val markerView = LayoutInflater.from(context).inflate(R.layout.custom_marker, null)
        val tagMarkerText = markerView.findViewById<TextView>(R.id.custom_marker_text)
        val tagMarkerImage = markerView.findViewById<ImageView>(R.id.custom_marker_image)

        //점포명이 너무 길면 미관상 별로라서 단축시켜서 표현
        val adjustedName = if(convenienceData.convenienceName.length > 8)
            "${convenienceData.convenienceName.substring(0, 8)}..."
        else
            convenienceData.convenienceName

        tagMarkerText.text = adjustedName


        if(selectedMarker){
            tagMarkerText.setBackgroundResource(R.drawable.convenience_info_border2_touch)
        }else{
            tagMarkerText.setBackgroundResource(R.drawable.convenience_info_border2)
        }


        val iconDrawable = when (convenienceData.convenienceType) {

            ConvenienceType.STORE_GS_25.title -> R.drawable.gs25_product_icon
            ConvenienceType.STORE_CU.title -> R.drawable.cu_product_icon
            ConvenienceType.STORE_SEVEN_ELEVEN.title -> R.drawable.seven_eleven_product_icon
            ConvenienceType.STORE_E_MART24.title -> R.drawable.emart24_product_icon
            else -> R.drawable.all_convenience_store
        }

        tagMarkerImage.setImageResource(iconDrawable)

        markerView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

        val bitmap = Bitmap.createBitmap(markerView.measuredWidth, markerView.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        markerView.draw(canvas)

        return bitmap
    }
}