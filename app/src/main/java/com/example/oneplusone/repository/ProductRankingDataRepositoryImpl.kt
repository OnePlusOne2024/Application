package com.example.oneplusone.repository

import com.example.oneplusone.serverConnection.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.util.Log
import com.example.oneplusone.model.data.ServerProductSearchRankingResult

class ProductRankingDataRepositoryImpl @Inject constructor()
    : ProductRankingDataRepository{

    override fun loadProductRankingData(callback: (ServerProductSearchRankingResult?) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.getProductSearchRanking()
                if (response.isSuccessful){
                    response.body()
                } else {
                    Log.d("연결 실패", "연결 실패")
                    null
                }
            } catch (e: Exception) {
                Log.d("연결 실패", e.toString())
                null
            }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }

    override fun postCurrentSearchText(searchText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitBuilder.api.postSearchText(searchText)

            } catch (e: Exception) {
                Log.d("검색어 전송 실패", e.toString())
            }
        }
    }


}