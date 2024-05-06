package com.example.oneplusone.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.R
import com.example.oneplusone.db.ProductDataBase
import com.example.oneplusone.model.data.ProductData

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
                "a",
                3000,
                "세븐 일레븐",
                "2+1",
                R.drawable.example_product_image,
                false,
                "과자",
                false
                ),
            ProductData(4,
                "a",
                1000,
                "세븐 일레븐",
                "1+1",
                R.drawable.example_product_image,
                false,
                "식품",
                false
                ),
            ProductData(5,
                "a",
                1500,
                "GS 25",
                "2+1",
                R.drawable.example_product_image,
                false,
                "아이스 크림",
                false
                ),
            ProductData(6,
                "a",
                1200,
                "GS 25",
                "1+1",
                R.drawable.example_product_image,
                false,
                "생활 용품",
                false
                ),
            ProductData(7,
                "a",
                111000,
                "이마트 24",
                "1+1",
                R.drawable.example_product_image,
                false,
                "아이스 크림",
                true
                ),
            ProductData(8,
                "a",
                11000,
                "이마트 24",
                "1+1",
                R.drawable.example_product_image,
                false,
                "아이스 크림",
                false
                ),
            ProductData(9,
                "a",
                50000,
                "세븐 일레븐",
                "1+1",
                R.drawable.example_product_image,
                false,
                "생활 용품",
                true
                ),
            ProductData(10,
                "a",
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
}