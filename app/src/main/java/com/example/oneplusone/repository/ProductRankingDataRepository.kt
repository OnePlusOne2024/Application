package com.example.oneplusone.repository

import com.example.oneplusone.model.data.ServerProductSearchRankingResult
import com.google.android.gms.location.LocationCallback

interface ProductRankingDataRepository {

    fun loadProductRankingData(callback: (ServerProductSearchRankingResult?) -> Unit)

    fun postCurrentSearchText(searchText:String)

//    fun loadProductRanking()
}