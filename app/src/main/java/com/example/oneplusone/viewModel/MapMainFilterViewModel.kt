package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.repository.MainFilterRepository
import com.example.oneplusone.repository.MapMainFilterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapMainFilterViewModel  @Inject constructor(
    private val mapMainFilterRepository: MapMainFilterRepository
): ViewModel() {

    private var _mainFilterDataList= MutableLiveData<List<MainFilterData>>()


    val mainFilterDataList: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList

    init {
        loadMainFilters()
    }

    private fun loadMainFilters() {
        _mainFilterDataList.value=mapMainFilterRepository.loadFilters()
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

    fun initMainFilters() {
        _mainFilterDataList.value=mapMainFilterRepository.loadFilters()
    }
}