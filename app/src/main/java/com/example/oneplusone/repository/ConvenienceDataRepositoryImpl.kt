package com.example.oneplusone.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.ServerConvenienceResult
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.serverConnection.RetrofitBuilder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConvenienceDataRepositoryImpl @Inject constructor() : ConvenienceDataRepository {

    private val convenienceData = MutableLiveData<List<ConvenienceData>>()

    override fun getConvenienceData(): LiveData<List<ConvenienceData>> = convenienceData

    override fun loadConvenienceData() {

        convenienceData.value= arrayListOf(


        )
    }

    override fun getConvenienceData(userCoordinate: LatLng, callback: (ServerConvenienceResult?) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = RetrofitBuilder.api.getConvenienceData(userCoordinate.latitude.toString(),userCoordinate.longitude.toString())

                if (response.isSuccessful){
                    Log.d("연결 성공","성공")
                    Log.d("연결 성공", response.body().toString())
                    response.body()
                } else {
                    Log.d("연결 실패","실패")
                    null
                }
            } catch (e: Exception) {
                Log.d("연결 실패", e.toString())
                null
            }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }

}