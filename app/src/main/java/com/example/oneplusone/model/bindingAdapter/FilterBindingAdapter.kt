package com.example.oneplusone.model.bindingAdapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.model.ProductFilterRecyclerAdapter
import com.example.oneplusone.model.data.FilterData

object FilterBindingAdapter {
    @JvmStatic
    @BindingAdapter("items")
    fun setFilterItems(recyclerView: RecyclerView, items: List<FilterData>) {

        if(recyclerView.adapter == null) {
            val adapter = ProductFilterRecyclerAdapter()
            recyclerView.adapter = adapter
        }
        val myAdapter = recyclerView.adapter as? ProductFilterRecyclerAdapter

        myAdapter?.submitList(items.toMutableList())
    }

    @JvmStatic
    @BindingAdapter("imageResource")
    fun setFilterImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

}