package com.example.oneplusone.repository

import com.example.oneplusone.model.data.ProductData

interface ProductRankingDataRepository {

    fun loadProductRankingData(): List<String>

//    fun loadProductRanking()
}