package com.example.oneplusone.model.data

import com.example.oneplusone.model.data.enum.FilterType

data class MainFilterData(
    val mainFilterImage: Int,
    val mainFilterText: String,
    val filterType: FilterType
)
