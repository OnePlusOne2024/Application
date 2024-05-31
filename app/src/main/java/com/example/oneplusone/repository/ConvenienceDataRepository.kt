package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.ServerConvenienceResult
import com.google.android.gms.maps.model.LatLng

interface ConvenienceDataRepository {

    fun getConvenienceData(userCoordinate: LatLng, callback: (ServerConvenienceResult?) -> Unit)
}