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

    private var _convenienceFilterImage = MutableLiveData<Int>()
    private var _convenienceFilterText = MutableLiveData<String>()

    private var _productCategoryFilterImage = MutableLiveData<Int>()
    private var _productCategoryFilterText = MutableLiveData<String>()

    private var _benefitsFilterImage = MutableLiveData<Int>()
    private var _benefitsFilterText = MutableLiveData<String>()

    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    val filterBar: LiveData<Boolean>
        get() = _filterBar




    //일단 초기 필터값
//        init{
//            _convenienceFilterImage.value =ConvenienceType.ALL_CONVENIENCE_STORE.iconResId
//            _convenienceFilterText.value = ConvenienceType.ALL_CONVENIENCE_STORE.title
//
//            _productCategoryFilterImage.value = ProductCategoryType.ALL_PRODUCT_CATEGORY.iconResId
//            _productCategoryFilterText.value =ProductCategoryType.ALL_PRODUCT_CATEGORY.title
//
//            _benefitsFilterImage.value = BenefitsType.ALL_BENEFITS.iconResId
//            _benefitsFilterText.value =BenefitsType.ALL_BENEFITS.title
//
//
//    }

    fun loadItems(filterType:FilterType) {

        //선택된 필터의 종류에 따라 필터 데이터에 넣어줌, 필터 타입도 넣어줌
        val loadItems = when (filterType) {
            FilterType.CONVENIENCE -> ConvenienceType.values().map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId,FilterType.CONVENIENCE)
            }
            FilterType.PRODUCT_CATEGORY -> ProductCategoryType.values().map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId,FilterType.PRODUCT_CATEGORY)
            }
            FilterType.BENEFITS -> BenefitsType.values().map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId,FilterType.BENEFITS)
            }
        }


//        _filterDataList.value = loadItems
        _filterDataList.value = loadItems
        Log.d("선택", filterDataList.value.toString())
        _filterBar.value=true
    }


}