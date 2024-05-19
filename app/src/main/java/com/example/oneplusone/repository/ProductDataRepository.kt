package com.example.oneplusone.repository

import com.example.oneplusone.model.data.ProductData

interface ProductDataRepository {

    fun loadProductData(): List<ProductData>



    fun getUpdateInfoCheck(lastConnectTime: Int?, callback: (Int?) -> Unit)
//    fun getGS25ProductList()
//
//    fun getCUProductList()
//
//    fun getSevenElevenProductList()
//
//    fun getEmart24ProductList()
}