package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import com.example.oneplusone.model.data.ConvenienceData

interface ConvenienceDataRepository {

    fun getConvenienceData(): LiveData<List<ConvenienceData>>

    fun loadConvenienceData()
}