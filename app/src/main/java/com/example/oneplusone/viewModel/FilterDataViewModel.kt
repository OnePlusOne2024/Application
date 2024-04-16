package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.R
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enum.BenefitsType
import com.example.oneplusone.model.data.enum.ConvenienceType
import com.example.oneplusone.model.data.enum.FilterType
import com.example.oneplusone.model.data.enum.ProductCategoryType


class FilterDataViewModel() : ViewModel() {

    private var _filterDataList= MutableLiveData<List<FilterData>>()
    private var _currentFilterCategory = MutableLiveData<String>()
    private var _filterBar=MutableLiveData<Boolean>()


    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    val currentFilterCategory: LiveData<String>
        get() = _currentFilterCategory

    val filterBar: LiveData<Boolean>
        get() = _filterBar

    init{
        val loadItems=
            listOf(
                FilterData("편의점 전체", R.drawable.all_convenience_store),
                FilterData("GS 25", R.drawable.gs_25),
                FilterData("CU", R.drawable.cu),
                FilterData("세븐 일레븐", R.drawable.seven_eleven),
                FilterData("이마트 24", R.drawable.emart_24),
            )

        _filterDataList.value = loadItems
    }

    private fun loadItems(filterType:FilterType) {


        Log.d("선택", filterType.toString())

        val loadItems = when (filterType) {
            FilterType.CONVENIENCE -> ConvenienceType.values().map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId)
            }
            FilterType.PRODUCT_CATEGORY -> ProductCategoryType.values().map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId)
            }
            FilterType.BENEFITS -> BenefitsType.values().map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId)
            }
        }
        Log.d("선택", loadItems.toString())

//        _filterDataList.value = loadItems
        _filterDataList.value = loadItems
        _filterBar.value=true
    }

    fun loadFilterItems(mainFilter: MainFilterData) {
        Log.d("필터타입", mainFilter.filterType.javaClass.name)
        loadItems(mainFilter.filterType)
    }


}