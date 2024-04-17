package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.R
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enum.BenefitsType
import com.example.oneplusone.model.data.enum.ConvenienceType
import com.example.oneplusone.model.data.enum.FilterType
import com.example.oneplusone.model.data.enum.ProductCategoryType

class MainFilterViewModel() : ViewModel() {

    private var _mainFilterDataList= MutableLiveData<List<MainFilterData>>()
    private val _selectedMainFilter = MutableLiveData<MainFilterData>()

    val mainFilterDataList: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList

    val selectedMainFilter: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList



    private var items= mutableListOf<MainFilterData>()

    init{
        items= arrayListOf(
            MainFilterData(
                ConvenienceType.ALL_CONVENIENCE_STORE.iconResId,
                ConvenienceType.ALL_CONVENIENCE_STORE.title,
                FilterType.CONVENIENCE
                ),
            MainFilterData(
                ProductCategoryType.ALL_PRODUCT_CATEGORY.iconResId,
                ProductCategoryType.ALL_PRODUCT_CATEGORY.title,
                FilterType.PRODUCT_CATEGORY
                ),
            MainFilterData(
                BenefitsType.ALL_BENEFITS.iconResId,
                BenefitsType.ALL_BENEFITS.title,
                FilterType.BENEFITS
                ),
        )
        _mainFilterDataList.postValue(items)
    }
    fun selectMainFilter(mainFilterData: MainFilterData) {
        _selectedMainFilter.value = mainFilterData
    }
}