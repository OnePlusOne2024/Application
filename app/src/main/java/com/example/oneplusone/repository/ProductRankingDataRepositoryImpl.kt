package com.example.oneplusone.repository

import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.PbType
import com.example.oneplusone.model.data.enums.ProductCategoryType
import javax.inject.Inject

class ProductRankingDataRepositoryImpl @Inject constructor()
    : ProductRankingDataRepository{

    override fun loadProductRankingData(): List<String> {
        return listOf(
            "스프라이트","펩시콜라","coke","apple","cap","bottle"
        )
    }
}