package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.repository.MainFilterRepository
import com.example.oneplusone.repository.MainFilterRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFilterViewModel @Inject constructor(
    private val mainFilterRepository: MainFilterRepository
): ViewModel() {

    val mainFilterDataList: LiveData<List<MainFilterData>> = mainFilterRepository.getFilters()

    init {
        loadMainFilters()
    }

    private fun loadMainFilters() {
        mainFilterRepository.loadFilters()
    }
}