package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData

interface ProductDataRepository {

    fun loadProductData(): List<ProductData>

    fun getUpdateInfoCheck()
    fun getGS25ProductList()

    fun getCUProductList()

    fun getSevenElevenProductList()

    fun getEmart24ProductList()
}