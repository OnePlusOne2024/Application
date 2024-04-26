package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.repository.ConvenienceDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapDataViewModel  @Inject constructor(

    private val convenienceDataRepository: ConvenienceDataRepository

): ViewModel() {


    val convenienceDataList: LiveData<List<ConvenienceData>> = convenienceDataRepository.getConvenienceData()

    private var _convenienceData= MutableLiveData<ConvenienceData>()



    val convenienceData: LiveData<ConvenienceData>
        get() = _convenienceData



    init{
        loadConvenienceData()
    }


    private  fun loadConvenienceData(){
        convenienceDataRepository.loadConvenienceData()
    }


}