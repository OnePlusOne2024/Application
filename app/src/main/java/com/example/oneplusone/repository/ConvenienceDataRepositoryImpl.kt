package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.enums.ConvenienceType
import javax.inject.Inject

class ConvenienceDataRepositoryImpl @Inject constructor() : ConvenienceDataRepository {

    private val convenienceData = MutableLiveData<List<ConvenienceData>>()

    override fun getConvenicenceData(): LiveData<List<ConvenienceData>> = convenienceData

    override fun loadConvenicenceData() {

        convenienceData.value= arrayListOf(
            ConvenienceData(ConvenienceType.STORE_GS_25.title,
                "GS25 대천점",
                37.514655,
                126.979974),

            ConvenienceData(ConvenienceType.STORE_SEVEN_ELEVEN.title,
                "세븐 일레븐 대천2점",
                37.516927,
                126.980288),
            )
    }
}