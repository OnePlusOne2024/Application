package com.example.oneplusone.repository

import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.ServerConvenienceResult
import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.model.data.ServerResponse
import com.google.android.gms.maps.model.LatLng

interface ProductDataRepository {

//    fun loadProductData(): List<ProductData>



    fun getUpdateInfoCheck(lastConnectTime: String?, callback: (Boolean) -> Unit)

    fun getProductDataList(lastConnectTime: String?,callback: (ServerResponse?) -> Unit)

    fun getConvenienceData(userCoordinate: LatLng,callback: (ServerConvenienceResult?) -> Unit)

//    fun getGS25ProductList()
//
//    fun getCUProductList()
//
//    fun getSevenElevenProductList()
//
//    fun getEmart24ProductList()
}