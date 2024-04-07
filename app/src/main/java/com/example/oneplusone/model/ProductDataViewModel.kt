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
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(2,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(3,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                true),
            ProductData(4,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(5,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(6,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(5,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(6,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(5,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
            ProductData(6,
                "a",
                1,
                "c",
                "d",
                byteArrayOf(),
                false),
        )
        _productDataList.postValue(items)
    }

}