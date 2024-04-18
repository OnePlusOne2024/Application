package com.example.oneplusone.repository

import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.enum.FilterType

interface FilterDataRepository {
    fun loadFilterData(filterType: FilterType): List<FilterData>
}