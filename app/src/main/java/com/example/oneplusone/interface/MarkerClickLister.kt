package com.example.oneplusone.`interface`

import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.ProductData

interface MarkerClickLister {

    fun onMarkerClick(convenienceData: ConvenienceData)
}
