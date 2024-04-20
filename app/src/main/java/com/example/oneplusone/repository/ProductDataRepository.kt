package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import com.example.oneplusone.model.data.ProductData

interface ProductDataRepository {

    fun getProductData(): LiveData<List<ProductData>>

    fun loadProductData()
}