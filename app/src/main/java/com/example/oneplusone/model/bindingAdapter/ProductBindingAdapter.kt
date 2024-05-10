package com.example.oneplusone.model.bindingAdapter

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.R
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.model.data.ProductData
//보류
object ProductBindingAdapter {
//    @JvmStatic
//    @BindingAdapter("productItems")
//    fun setProductItems(recyclerView: RecyclerView, items: List<ProductData>?) {
//        if(recyclerView.adapter == null) {
//
//            recyclerView.adapter = ProductItemRecyclerAdapter(object : ProductClickListener {
//                override fun onItemClick(productData: ProductData) {
//                    Log.d("ProductClick", "Clicked Product ID: ${productData.productId}")
//                }
//            })
//        }
//
//        val adapter = recyclerView.adapter as? ProductItemRecyclerAdapter
//        adapter?.submitList(items ?: emptyList())
//    }

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
    fun setProductImage(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("brandImage")
    fun setBrandImage(imageView: ImageView, resource: String) {
        when (resource) {
            "GS 25" -> imageView.setImageResource(R.drawable.gs25_product_icon)
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

    @JvmStatic
    @BindingAdapter("recentSearchText")
    fun setRecentSearch(view: TextView, resource: String) {
        view.text=resource
    }

}