package com.example.oneplusone.repository

import android.util.Log
import com.example.oneplusone.R
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.serverConnection.RetrofitBuilder
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response

import javax.inject.Inject


class ProductDataRepositoryImpl @Inject constructor(

) : ProductDataRepository {

    override fun loadProductData():List<ProductData> {

        return listOf(
            ProductData(1,
                "스프라이트",
                1000,
                "CU",
                "1+1",
                R.drawable.example_product_image,
                false,
                "음료",
                false,
                ),
            ProductData(2,
                "펩시콜라",
                2000,
                "CU",
                "1+1",
                R.drawable.example_product_image,
                false,
                "음료",
                true
                ),
            ProductData(3,
                "coke",
                3000,
                "세븐 일레븐",
                "2+1",
                R.drawable.example_product_image,
                false,
                "과자",
                false
                ),
            ProductData(4,
                "apple",
                1000,
                "세븐 일레븐",
                "1+1",
                R.drawable.example_product_image,
                false,
                "식품",
                false
                ),
            ProductData(5,
                "ball",
                1500,
                "GS 25",
                "2+1",
                R.drawable.example_product_image,
                false,
                "아이스 크림",
                false
                ),
            ProductData(6,
                "cap",
                1200,
                "GS 25",
                "1+1",
                R.drawable.example_product_image,
                false,
                "생활 용품",
                false
                ),
            ProductData(7,
                "water",
                111000,
                "이마트 24",
                "1+1",
                R.drawable.example_product_image,
                false,
                "아이스 크림",
                true
                ),
            ProductData(8,
                "pepsi",
                11000,
                "이마트 24",
                "1+1",
                R.drawable.example_product_image,
                false,
                "아이스 크림",
                false
                ),
            ProductData(9,
                "bottle",
                50000,
                "세븐 일레븐",
                "1+1",
                R.drawable.example_product_image,
                false,
                "생활 용품",
                true
                ),
            ProductData(10,
                "cup",
                600000,
                "GS 25",
                "할인",
                R.drawable.example_product_image,
                false,
                "음료",
                false
                ),
        )
    }

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

    override fun getProductDataList(callback: (List<ServerProductData>?) -> Unit) {


        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.getProductList()
                if (response.isSuccessful){
                    Log.d("연결 성공","성공")
                    response.body()?.result
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