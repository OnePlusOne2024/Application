package com.example.oneplusone.model.bindingAdapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

import com.example.oneplusone.R

import java.net.URL
import java.util.concurrent.Executors

//보류
object ProductBindingAdapter {

    @JvmStatic
    @BindingAdapter("benefitsImage")
    fun setBenefitsImage(imageView: ImageView, resource: String) {
        when (resource) {
            "1+1" -> imageView.setImageResource(R.drawable.one_plus_one_product_icon)
            "2+1" -> imageView.setImageResource(R.drawable.two_plus_one_product_icon)
            "3+1" -> imageView.setImageResource(R.drawable.three_plus_one_product_icon)
            "할인" -> imageView.setImageResource(R.drawable.discount_product_icon)
            else -> imageView.setImageResource(R.drawable.discount_product_icon)
        }
    }

    @JvmStatic
    @BindingAdapter("productImage")
    fun setProductImage(imageView: ImageView, resource: String) {
        Log.d("resource",resource)
        Glide.with(imageView.context)
            .load(resource)
            .into(imageView)
//        imageView.setImageResource(image)
    }

    @JvmStatic
    @BindingAdapter("brandImage")
    fun setBrandImage(imageView: ImageView, resource: String) {
        when (resource) {
            "GS25" -> imageView.setImageResource(R.drawable.gs25_product_icon)
            "CU" -> imageView.setImageResource(R.drawable.cu_product_icon)
            "세븐 일레븐" -> imageView.setImageResource(R.drawable.seven_eleven_product_icon)
            "이마트 24" -> imageView.setImageResource(R.drawable.emart24_product_icon)
            else -> imageView.setImageResource(R.drawable.gs25_product_icon)
        }
    }

    @JvmStatic
    @BindingAdapter("productName")
    fun setProductName(view: TextView, resource: String) {
        view.text=resource
    }

    @JvmStatic
    @BindingAdapter("productPrice")
    fun setProductPrice(view: TextView, resource: String) {
        view.text=resource.toString()
    }

    @JvmStatic
    @BindingAdapter("favoriteImage")
    fun setFavoriteImage(imageView: ImageView, resource: Boolean) {
        when (resource) {
            true -> imageView.setImageResource(R.drawable.favorite_on)
            false -> imageView.setImageResource(R.drawable.favorite_off)
        }
    }


}