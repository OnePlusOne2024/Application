package com.example.oneplusone.`interface`

import android.view.View
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData

interface MainFilterClickListener {
    fun onMainFilterClick(mainFilter: MainFilterData,itemView: View)
}