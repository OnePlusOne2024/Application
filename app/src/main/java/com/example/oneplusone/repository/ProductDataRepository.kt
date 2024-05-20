package com.example.oneplusone.repository

import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.ServerProductData

interface ProductDataRepository {

    fun loadProductData(): List<ProductData>



    fun getUpdateInfoCheck(lastConnectTime: String?, callback: (Boolean) -> Unit)

    fun getProductDataList(callback: (List<ServerProductData>?) -> Unit)

//    fun getGS25ProductList()
//
//    fun getCUProductList()
//
//    fun getSevenElevenProductList()
//
//    fun getEmart24ProductList()
}