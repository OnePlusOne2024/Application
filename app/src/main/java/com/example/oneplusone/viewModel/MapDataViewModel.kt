package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.repository.ConvenienceDataRepository
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapDataViewModel  @Inject constructor(

    private val convenienceDataRepository: ConvenienceDataRepository

): ViewModel() {

    //_convenienceDataList는 관찰 가능한 데이터를 제공
    private val _convenienceDataList: LiveData<List<ConvenienceData>> = convenienceDataRepository.getConvenienceData()

    private val _selectedMarkerData = MutableLiveData<ConvenienceData?>()



    //loadConvenienceData()는 데이터를 로딩하는 데 필요
    val convenienceDataList: LiveData<List<ConvenienceData>>
        get() = _convenienceDataList

    val selectedMarkerData: LiveData<ConvenienceData?>
        get() = _selectedMarkerData

    init{
        loadConvenienceData()
    }

    private  fun loadConvenienceData(){
        convenienceDataRepository.loadConvenienceData()
    }

    fun selectMarker(convenienceData: ConvenienceData?) {
        if (_selectedMarkerData.value == convenienceData) {

            _selectedMarkerData.value = null
        } else {
            // 새로운 마커 선택
            _selectedMarkerData.value = convenienceData
        }
    }

}