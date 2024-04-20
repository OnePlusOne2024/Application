package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.repository.FilterDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterDataViewModel @Inject internal constructor(
    private val repository: FilterDataRepository
) : ViewModel() {

    private var _filterDataList = MutableLiveData<List<FilterData>>()
    private var _filterBar = MutableLiveData<Boolean>()

    private var _filterType = MutableLiveData<FilterType>()


    //데이터를 감시할때 사용함
    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    val filterBar: LiveData<Boolean>
        get() = _filterBar

    private val filterType: LiveData<FilterType>
        get()=_filterType

    fun setFilterType(selectFilterType: FilterType) {

        loadItems(selectFilterType)
    }

    private fun loadItems(selectFilterType:FilterType) {
        // 이미 선택된 메인 필터를 다시 클릭한 경우 세부 필터를 제거
        Log.d("_filterDataList.value", _filterDataList.value.toString())
        if (_filterType.value == selectFilterType) {
            Log.d("_filterDataList.value2", _filterType.value.toString())
            clearFilterData()
        } else {
            val loadItems = repository.loadFilterData(selectFilterType)

            _filterDataList.value = loadItems
            _filterType.value = selectFilterType
            updateFilterBar(true)
        }
    }

    fun clearFilterData() {
        _filterDataList.value = emptyList()
        updateFilterBar(false)
        _filterType.value = FilterType.NONE
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
