package com.example.oneplusone.repository

import android.util.Log
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.ServerConvenienceResult
import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.model.data.ServerResponse
import com.example.oneplusone.serverConnection.RetrofitBuilder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject


class ProductDataRepositoryImpl @Inject constructor(

) : ProductDataRepository {


//고차함수 제대로 이해하기
    override fun getUpdateInfoCheck(lastConnectTime: String?, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.updateInfoCheck(lastConnectTime)
                if (response.isSuccessful){
                    response.body()
                } else null
            } catch (e: Exception) {
                null
            }
            withContext(Dispatchers.Main) {
                if (result != null) {
                    callback(result)
                }
            }
        }
    }

    override fun getProductDataList(lastConnectTime: String?,callback: (ServerResponse?) -> Unit) {

        if (lastConnectTime != null) {
            Log.d("lastConnectTime",lastConnectTime)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.getProductList(lastConnectTime)

                if (response.isSuccessful){
                    Log.d("연결 성공","성공")
                    Log.d("연결 성공", response.body().toString())
                    response.body()
                } else {
                    Log.d("연결 실패", response.body().toString())
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

    override fun getConvenienceData(userCoordinate: LatLng, callback: (ServerConvenienceResult?) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.getConvenienceData(userCoordinate.latitude.toString(),userCoordinate.longitude.toString())

                if (response.isSuccessful){
                    Log.d("연결 성공","성공")
                    Log.d("연결 성공", response.body().toString())
                    response.body()
                } else {
                    Log.d("연결 실패","실패")
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
//    override fun getGS25ProductList() {
//        RetrofitBuilder.api.getGS25ProductData().enqueue(object : Callback<List<ProductData>> {
//            override fun onResponse(call: Call<List<ProductData>>, response: Response<List<ProductData>>) {
//                if (response.isSuccessful) {
//                    val gs25ProductDataList = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<List<ProductData>>, t: Throwable) {
//            }
//        })
//    }
//
//    override fun getCUProductList() {
//        RetrofitBuilder.api.getCUProductData().enqueue(object : Callback<List<ProductData>> {
//            override fun onResponse(call: Call<List<ProductData>>, response: Response<List<ProductData>>) {
//                if (response.isSuccessful) {
//                    val cuProductDataList = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<List<ProductData>>, t: Throwable) {
//            }
//        })
//    }
//    override fun getSevenElevenProductList() {
//        RetrofitBuilder.api.getSevenElevenProductData().enqueue(object : Callback<List<ProductData>> {
//            override fun onResponse(call: Call<List<ProductData>>, response: Response<List<ProductData>>) {
//                if (response.isSuccessful) {
//                    val sevenElevenProductDataList = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<List<ProductData>>, t: Throwable) {
//            }
//        })
//    }
//    override fun getEmart24ProductList() {
//        RetrofitBuilder.api.getEmart24ProductData().enqueue(object : Callback<List<ProductData>> {
//            override fun onResponse(call: Call<List<ProductData>>, response: Response<List<ProductData>>) {
//                if (response.isSuccessful) {
//                    val emart24ProductDataList = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<List<ProductData>>, t: Throwable) {
//
//            }
//        })
//    }
}