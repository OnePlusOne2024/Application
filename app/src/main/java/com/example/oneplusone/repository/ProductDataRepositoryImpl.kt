package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.R
import com.example.oneplusone.model.data.ProductData

import javax.inject.Inject

class ProductDataRepositoryImpl @Inject constructor() : ProductDataRepository {
    private val productDataList = MutableLiveData<List<ProductData>>()
    override fun getProductData(): LiveData<List<ProductData>> = productDataList

    override fun loadProductData() {

        productDataList.value = arrayListOf(
            ProductData(1,
                "스프라이트",
                1000,
                "cu",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(2,
                "펩시콜라",
                2000,
                "cu",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(3,
                "a",
                3000,
                "seven_eleven",
                "twoPlusOne",
                R.drawable.example_product_image,
                true),
            ProductData(4,
                "a",
                1000,
                "seven_eleven",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(5,
                "a",
                1500,
                "gs25",
                "twoPlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(6,
                "a",
                1200,
                "gs25",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(7,
                "a",
                111000,
                "emart24",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(8,
                "a",
                11000,
                "emart24",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(9,
                "a",
                50000,
                "seven_eleven",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
            ProductData(10,
                "a",
                600000,
                "gs25",
                "onePlusOne",
                R.drawable.example_product_image,
                false),
        )
    }
}