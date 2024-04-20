package com.example.oneplusone.model.bindingAdapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter

//보류
object MainFilterBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["mainFilterItems"])
    fun setMainFilterItems(recyclerView: RecyclerView, items: List<MainFilterData>?) {

        //처음 시작할 때 생성
//        if(recyclerView.adapter == null) {
//
//            recyclerView.adapter = MainFilterRecyclerAdapter(object : MainFilterClickListener {
//                override fun onMainFilterClick(mainFilter: MainFilterData) {
//                    FilterDataViewModel().loadFilterItems(mainFilter)
//
//                }
//            })
//        }

        val adapter = recyclerView.adapter as? MainFilterRecyclerAdapter
        adapter?.submitList(items)

    }

    @JvmStatic
    @BindingAdapter("mainFilterImage")
    fun setFilterImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("mainFilterText")
    fun setFilterText(view: TextView, resource: String) {
        view.text = resource
    }

}
