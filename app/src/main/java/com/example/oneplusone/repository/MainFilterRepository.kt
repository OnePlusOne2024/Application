package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.MainFilterData

interface MainFilterRepository {

    fun loadFilters(): List<MainFilterData>
}