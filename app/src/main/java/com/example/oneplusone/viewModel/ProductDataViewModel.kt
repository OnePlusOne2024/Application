package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.R
import com.example.oneplusone.model.data.ProductData

//productDataRepository : ProductDataRepository

class ProductDataViewModel :ViewModel() {
    private var _productDataList= MutableLiveData<List<ProductData>>()
    val productDataList : LiveData<List<ProductData>>
        get()=_productDataList
    //todo 서버에서 데이터를 가져왔을 경우를 생각해 레포지토리 생성하기
    private var items= mutableListOf<ProductData>()
    init{
        items= arrayListOf(
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
                "d",
                R.drawable.example_product_image,
                false),
            ProductData(3,
                "a",
                3000,
                "seven_eleven",
                "onePlusOne",
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
        _productDataList.postValue(items)
    }

}