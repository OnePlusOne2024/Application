package com.example.oneplusone.model.bindingAdapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.model.ProductFilterRecyclerAdapter
import com.example.oneplusone.model.data.FilterData

object FilterBindingAdapter {
    @JvmStatic
    @BindingAdapter("filterItems")
    fun setFilterItems(recyclerView: RecyclerView, items: List<FilterData>?) {
//java.lang.RuntimeException: Failed to call observer method 에러의 원인: 처음 어플을 실행하고 터치하지 않았을때는
//items: List<FilterData>가 null이기때문에 앱이 크래쉬됐던것 null을 허용했어야했음


        if(recyclerView.adapter == null) {
            recyclerView.adapter = ProductFilterRecyclerAdapter()
        }

        val adapter = recyclerView.adapter as? ProductFilterRecyclerAdapter
        adapter?.submitList(items ?: emptyList())
    }

    @JvmStatic
    @BindingAdapter("filterImage")
    fun setFilterImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("filterText")
    fun setFilterText(view: TextView, resource: String) {
        view.text=resource
    }
}