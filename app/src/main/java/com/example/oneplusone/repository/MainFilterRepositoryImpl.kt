package com.example.oneplusone.repository

import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.ProductCategoryType
import javax.inject.Inject


class MainFilterRepositoryImpl @Inject constructor()
    : MainFilterRepository {


    override fun loadFilters(): List<MainFilterData> {
        // 필터 데이터 로드
        return listOf(
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
            )
        )
    }
}