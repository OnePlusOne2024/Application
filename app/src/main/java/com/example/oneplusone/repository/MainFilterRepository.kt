package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enum.BenefitsType
import com.example.oneplusone.model.data.enum.ConvenienceType
import com.example.oneplusone.model.data.enum.FilterType
import com.example.oneplusone.model.data.enum.ProductCategoryType



class MainFilterRepository {

    private val filters = MutableLiveData<List<MainFilterData>>()

    fun getFilters(): LiveData<List<MainFilterData>> = filters

    fun loadFilters() {

        filters.value = listOf(
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
    }


}