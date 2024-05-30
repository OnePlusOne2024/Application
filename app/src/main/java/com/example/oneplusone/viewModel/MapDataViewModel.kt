package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.ServerConvenienceData
import com.example.oneplusone.repository.ConvenienceDataRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapDataViewModel  @Inject constructor(

    private val convenienceDataRepository: ConvenienceDataRepository

): ViewModel() {

    //_convenienceDataList는 관찰 가능한 데이터를 제공
    private val _convenienceDataList= MutableLiveData<List<ConvenienceData>>()

    private val _selectedMarkerData = MutableLiveData<ConvenienceData?>()



    //loadConvenienceData()는 데이터를 로딩하는 데 필요
    val convenienceDataList: LiveData<List<ConvenienceData>>
        get() = _convenienceDataList

    val selectedMarkerData: LiveData<ConvenienceData?>
        get() = _selectedMarkerData


    fun selectMarker(convenienceData: ConvenienceData?) {
        if (_selectedMarkerData.value == convenienceData) {

            _selectedMarkerData.value = null
        } else {
            // 새로운 마커 선택
            _selectedMarkerData.value = convenienceData
        }
    }

    fun loadConvenienceDataFromServer(userCoordinate: LatLng){
        convenienceDataRepository.getConvenienceData(userCoordinate) { serverResult ->
            if(serverResult!=null && serverResult.success){
                _convenienceDataList.value=convertConvenienceDataType(serverResult.result)
            }
            Log.d("serverResult", serverResult.toString())
        }
    }

    private fun convertConvenienceDataType(serverConvenienceData: List<ServerConvenienceData>): List<ConvenienceData> {
        return serverConvenienceData.map { convenience ->
            ConvenienceData(
                convenienceType=convenience.convBrandName,
                convenienceName = convenience.convName,
                convenienceAddress = convenience.convAddr,
                conveniencePosition = LatLng(convenience.latitude,convenience.longitude)
            )
        }
    }

}