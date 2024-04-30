package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.repository.MainFilterRepository
import com.example.oneplusone.repository.MapMainFilterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapMainFilterViewModel  @Inject constructor(
    private val mapMainFilterRepository: MapMainFilterRepository
): ViewModel() {

    val mainFilterDataList: LiveData<List<MainFilterData>> = mapMainFilterRepository.getFilters()

    init {
        loadMainFilters()
    }

    private fun loadMainFilters() {
        mapMainFilterRepository.loadFilters()
    }
}