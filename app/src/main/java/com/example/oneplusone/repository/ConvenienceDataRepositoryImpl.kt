package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class ConvenienceDataRepositoryImpl @Inject constructor() : ConvenienceDataRepository {

    private val convenienceData = MutableLiveData<List<ConvenienceData>>()

    override fun getConvenienceData(): LiveData<List<ConvenienceData>> = convenienceData

    override fun loadConvenienceData() {

        convenienceData.value= arrayListOf(
            ConvenienceData(ConvenienceType.STORE_GS_25.title,
                "GS25 대천",
                LatLng(37.514655, 126.979974),
                ),

            ConvenienceData(ConvenienceType.STORE_SEVEN_ELEVEN.title,
                "세븐 일레븐 대천2점 울트라",
                LatLng(37.516927,126.980288),
            ),
            ConvenienceData(ConvenienceType.STORE_CU.title,
                "CU 아산5호점",
                LatLng(37.514655, 126.983000),
            ),
            ConvenienceData(ConvenienceType.STORE_E_MART24.title,
                "이마트 24 순천",
                LatLng(37.514655, 126.981100),
            ),
            ConvenienceData(ConvenienceType.STORE_GS_25.title,
                "GS25 대천5",
                LatLng(37.516927,126.982200),
            ),

        )
    }

}