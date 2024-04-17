package com.example.oneplusone.`interface`

import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.enum.FilterType

interface FilterClickListener {
    fun onFilterClick(filterData: FilterData)
}