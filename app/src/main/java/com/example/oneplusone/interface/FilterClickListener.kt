package com.example.oneplusone.`interface`

import android.util.Log
import com.example.oneplusone.model.data.FilterData

interface FilterClickListener {
    fun onFilterClick(filterData: FilterData)
}