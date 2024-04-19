package com.example.oneplusone.viewModel

import android.util.Log
import android.widget.Switch
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
    private var currentMainFilterType: FilterType? = null
    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    val filterBar: LiveData<Boolean>
        get() = _filterBar

    fun loadItems(filterType: FilterType) {
        // 이미 선택된 메인 필터를 다시 클릭한 경우 세부 필터를 제거
        if (filterType == currentMainFilterType) {
            clearFilterData()
        } else {
            val loadItems = repository.loadFilterData(filterType)
            _filterDataList.value = loadItems
            updateFilterBar(true)
            currentMainFilterType = filterType
        }
    }

    fun clearFilterData() {
        _filterDataList.value = emptyList()
        updateFilterBar(false)
        currentMainFilterType = null
    }

    private fun updateFilterBar(isVisible: Boolean) {
        _filterBar.value = isVisible
    }
}



//    fun filterBarSwitch() {
//        Log.d("필터바 초기값", filterBar.value.toString())
//
//
//        if(_filterBar.value==null){
//
//            _filterBar.value=true
//
//        }else if(_filterBar.value==true){
//
//            _filterBar.value=false
//
//        }else if(_filterBar.value==false){
//
//            _filterBar.value=true
//
//        }
//    }
