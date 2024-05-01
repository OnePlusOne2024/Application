package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.repository.MainFilterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFilterViewModel @Inject internal constructor(
    private val mainFilterRepository: MainFilterRepository
): ViewModel() {

    private var _mainFilterDataList= MutableLiveData<List<MainFilterData>>()

    private var _currentMainFilterDataList = MutableLiveData<List<MainFilterData>>()

    //데이터를 감시할때 사용함

    val mainFilterDataList: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList

//    val currentMainFilterDataList: LiveData<List<MainFilterData>>
//        get() = _currentMainFilterDataList

    init {
        loadMainFilters()
    }

    private fun loadMainFilters() {
        _mainFilterDataList.value=mainFilterRepository.loadFilters()
    }

    fun updateMainFilter(filterData: FilterData) {
        val currentList = _mainFilterDataList.value ?: listOf()
        val updatedList = currentList.toMutableList()

        for(index in updatedList.indices){
            if (updatedList[index].filterType == filterData.filterType) {

                val newItem = MainFilterData(
                    mainFilterImage = filterData.filterImage,
                    mainFilterText = filterData.filterText,
                    filterType = filterData.filterType
                )
                updatedList[index] = newItem
                break
            }
        }

        _mainFilterDataList.value = updatedList
        Log.d("_mainFilterDataList", _mainFilterDataList.value.toString())
    }

}