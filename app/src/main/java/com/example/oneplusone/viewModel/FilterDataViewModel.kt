package com.example.oneplusone.viewModel

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.R
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
    private var _selectFilterColorSwitch = MutableLiveData<Boolean>()

    //데이터를 감시할때 사용함
    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    val filterBar: LiveData<Boolean>
        get() = _filterBar

    private val filterType: LiveData<FilterType>
        get()=_filterType

    val selectFilterColorSwitch: LiveData<Boolean>
        get()=_selectFilterColorSwitch

    fun showFilter(selectFilterType: FilterType) {

        loadItems(selectFilterType)

    }

    private fun loadItems(selectFilterType:FilterType) {

        // 이미 선택된 메인 필터를 다시 클릭한 경우 세부 필터를 제거
        if (_filterType.value == selectFilterType) {

            clearFilterData()

        } else {

            val loadItems = repository.loadFilterData(selectFilterType)
            _filterDataList.value = loadItems
            _filterType.value = selectFilterType
            updateFilterBar(true)
            updateFilterColorSwitch(true)
        }
    }

    fun clearFilterData() {
        _filterDataList.value = emptyList()
        updateFilterBar(false)
        updateFilterColorSwitch(false)
        _filterType.value = FilterType.NONE
    }

    private fun updateFilterBar(isVisible: Boolean) {
        _filterBar.value = isVisible
    }


    private fun updateFilterColorSwitch(boolean: Boolean) {
        _selectFilterColorSwitch.value = boolean
    }
}

