package com.example.oneplusone.model.bindingAdapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.FilterDataViewModel

object FilterBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["filterItems"])
    fun setFilterItems(recyclerView: RecyclerView, items: List<FilterData>?) {
//java.lang.RuntimeException: Failed to call observer method 에러의 원인: 처음 어플을 실행하고 터치하지 않았을때는
//items: List<FilterData>가 null이기때문에 앱이 크래쉬됐던것 null을 허용했어야했음


        //처음 시작할 때 생성
        if(recyclerView.adapter == null) {

            recyclerView.adapter = ProductFilterRecyclerAdapter(object : FilterClickListener {
                override fun onFilterClick(filterData: FilterData) {
                    Log.d("FilterClick", "Clicked Filter Name: ${filterData.filterText}")
                }
            })
        }

        val adapter = recyclerView.adapter as? ProductFilterRecyclerAdapter
        adapter?.submitList(items)

//        if (filterCategory == FilterDataViewModel.CONVENIENCE_STORE) {
//            val decoration = ItemSpacingController(25, 25, 0)
//            recyclerView.addItemDecoration(decoration)
//        } else {
//            while (recyclerView.itemDecorationCount > 0) {
//                recyclerView.removeItemDecorationAt(0)
//            }
//        }
    }

    @JvmStatic
    @BindingAdapter("filterImage")
    fun setFilterImageResource(imageView: ImageView, resource: Int) {
        Log.d("시작","시작")
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("filterText")
    fun setFilterText(view: TextView, resource: String) {
        view.text=resource
    }

    @JvmStatic
    @BindingAdapter("filterBar")
    fun setFilterBar(view: ImageView, resource: Boolean) {
        if(resource){
            view.visibility = View.VISIBLE
        }
        else{
            view.visibility = View.INVISIBLE
        }
    }

}