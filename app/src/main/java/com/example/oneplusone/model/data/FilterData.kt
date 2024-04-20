package com.example.oneplusone.model.data

import com.example.oneplusone.model.data.enums.FilterType


data class FilterData(
    val filterText:String,

    val filterImage: Int,

    val filterType: FilterType
    )
