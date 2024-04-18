package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.R
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enum.BenefitsType
import com.example.oneplusone.model.data.enum.ConvenienceType
import com.example.oneplusone.model.data.enum.FilterType
import com.example.oneplusone.model.data.enum.ProductCategoryType
import com.example.oneplusone.repository.FilterDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterDataViewModel @Inject internal constructor(
    private val repository: FilterDataRepository
) : ViewModel() {

    private var _filterDataList = MutableLiveData<List<FilterData>>()
    private var _filterBar = MutableLiveData<Boolean>()

    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    val filterBar: LiveData<Boolean>
        get() = _filterBar

    fun loadItems(filterType: FilterType) {
        val loadItems = repository.loadFilterData(filterType)
        _filterDataList.value = loadItems
        _filterBar.value = true
    }

}