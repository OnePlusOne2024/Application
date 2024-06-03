package com.example.oneplusone.repository

import com.example.oneplusone.model.data.ProductRanking
import com.example.oneplusone.model.data.ServerProductSearchRankingResult
import com.google.android.gms.location.LocationCallback
import okhttp3.ResponseBody
import retrofit2.Response

interface ProductRankingDataRepository {

    fun loadProductRankingData(callback: (List<ProductRanking>?) -> Unit)

    fun postCurrentSearchText(searchText:String)

//    fun loadProductRanking()
}