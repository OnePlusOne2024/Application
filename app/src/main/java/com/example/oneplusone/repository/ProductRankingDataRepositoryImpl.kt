package com.example.oneplusone.repository

import com.example.oneplusone.serverConnection.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.util.Log
import com.example.oneplusone.model.data.ProductRanking
import com.example.oneplusone.model.data.ServerProductSearchRankingResult
import okhttp3.ResponseBody

class ProductRankingDataRepositoryImpl @Inject constructor()
    : ProductRankingDataRepository{

    override fun loadProductRankingData(callback: (List<ProductRanking>?) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.getProductSearchRanking()
                if (response.isSuccessful){

                    Log.d("상품 검색 랭킹 받아오기 성공", response.body().toString())
                    response.body()
                } else {
                    Log.d("상품 검색 랭킹 받아오기 실패", response.body().toString())
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
        Log.d("보낸searchText",searchText)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response=RetrofitBuilder.api.postSearchText(searchText)
                if (response.isSuccessful){
                    Log.d("검색어 서버로 전송 성공", response.body()?.string()?:"서버에서 응답 없음")
                } else {
                    Log.d("검색어 서버로 전송 실패", "연결 실패")
                }
            } catch (e: Exception) {
                Log.d("검색어 전송 실패", e.toString())
            }
        }
    }


}