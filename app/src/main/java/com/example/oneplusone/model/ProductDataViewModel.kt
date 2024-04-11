package com.example.oneplusone.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.ProductData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
//productDataRepository : ProductDataRepository

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
                0,
                false),
            ProductData(2,
                "펩시콜라",
                2000,
                "cu",
                "d",
                0,
                false),
            ProductData(3,
                "a",
                3000,
                "seven_eleven",
                "onePlusOne",
                0,
                true),
            ProductData(4,
                "a",
                1000,
                "seven_eleven",
                "onePlusOne",
                0,
                false),
            ProductData(5,
                "a",
                1500,
                "gs25",
                "twoPlusOne",
                0,
                false),
            ProductData(6,
                "a",
                1200,
                "gs25",
                "onePlusOne",
                0,
                false),
            ProductData(7,
                "a",
                111000,
                "emart24",
                "onePlusOne",
                0,
                false),
            ProductData(8,
                "a",
                11000,
                "emart24",
                "onePlusOne",
                0,
                false),
            ProductData(9,
                "a",
                50000,
                "seven_eleven",
                "onePlusOne",
                0,
                false),
            ProductData(10,
                "a",
                600000,
                "gs25",
                "onePlusOne",
                0,
                false),
        )
        _productDataList.postValue(items)
    }

}