package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.repository.ProductDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

): ViewModel() {

    private val _productNameList=MutableLiveData<ArrayList<String>>()

    val productNameList: LiveData<ArrayList<String>>
        get() = _productNameList


    fun updateProductNameList(productNames: ArrayList<String>) {
        _productNameList.value = productNames
    }
}