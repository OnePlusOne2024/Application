package com.example.oneplusone.repository

import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.PbType
import com.example.oneplusone.model.data.enums.ProductCategoryType
import com.example.oneplusone.serverConnection.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProductRankingDataRepositoryImpl @Inject constructor()
    : ProductRankingDataRepository{

    override fun loadProductRankingData(): List<String> {
        return listOf(
            "스프라이트","펩시콜라","coke","apple","cap","bottle"
        )
    }

    override fun loadProductRanking() {
        RetrofitBuilder.api.getProductRanking().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val productRankingList = response.body()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
            }
        })
    }
}