package com.example.oneplusone.repository

import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.enum.BenefitsType
import com.example.oneplusone.model.data.enum.ConvenienceType
import com.example.oneplusone.model.data.enum.FilterType
import com.example.oneplusone.model.data.enum.ProductCategoryType
import javax.inject.Inject

class FilterDataRepositoryImpl @Inject constructor()
    : FilterDataRepository {
    override fun loadFilterData(filterType: FilterType): List<FilterData> {
        return when (filterType) {
            FilterType.CONVENIENCE -> ConvenienceType.entries.map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId, FilterType.CONVENIENCE)
            }
            FilterType.PRODUCT_CATEGORY -> ProductCategoryType.entries.map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId, FilterType.PRODUCT_CATEGORY)
            }
            FilterType.BENEFITS -> BenefitsType.entries.map { enumItem ->
                FilterData(enumItem.title, enumItem.iconResId, FilterType.BENEFITS)
            }
        }
    }
}