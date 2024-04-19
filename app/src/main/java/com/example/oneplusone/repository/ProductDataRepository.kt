package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enum.FilterType

interface ProductDataRepository {

    fun getProductData(): LiveData<List<ProductData>>

    fun loadProductData()
}