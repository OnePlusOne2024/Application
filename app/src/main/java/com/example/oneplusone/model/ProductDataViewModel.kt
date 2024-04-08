package com.example.oneplusone.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
//productDataRepository : ProductDataRepository
@HiltViewModel
class ProductDataViewModel @Inject constructor()
    :ViewModel() {
    private var _productDataList= MutableLiveData<List<ProductData>>()
    val productDataList : LiveData<List<ProductData>>
        get()=_productDataList

    private var items= mutableListOf<ProductData>()
    init{
        items= arrayListOf(
            ProductData(1,
                "스프라이트",
                1000,
                "cu",
                "onePlusOne",
                byteArrayOf(),
                false),
            ProductData(2,
                "펩시콜라",
                2000,
                "cu",
                "d",
                byteArrayOf(),
                false),
            ProductData(3,
                "a",
                3000,
                "seven_eleven",
                "onePlusOne",
                byteArrayOf(),
                true),
            ProductData(4,
                "a",
                1000,
                "seven_eleven",
                "onePlusOne",
                byteArrayOf(),
                false),
            ProductData(5,
                "a",
                1500,
                "gs25",
                "twoPlusOne",
                byteArrayOf(),
                false),
            ProductData(6,
                "a",
                1200,
                "gs25",
                "onePlusOne",
                byteArrayOf(),
                false),
            ProductData(7,
                "a",
                111000,
                "emart24",
                "onePlusOne",
                byteArrayOf(),
                false),
            ProductData(8,
                "a",
                11000,
                "emart24",
                "onePlusOne",
                byteArrayOf(),
                false),
            ProductData(9,
                "a",
                50000,
                "seven_eleven",
                "onePlusOne",
                byteArrayOf(),
                false),
            ProductData(10,
                "a",
                600000,
                "gs25",
                "onePlusOne",
                byteArrayOf(),
                false),
        )
        _productDataList.postValue(items)
    }

}