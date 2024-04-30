package com.example.oneplusone.viewModel

import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.repository.ProductDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//productDataRepository : ProductDataRepository

@HiltViewModel
class ProductDataViewModel @Inject constructor(
    //소스를 가져오거나 저장하는 담당
    private val productDataRepository: ProductDataRepository

):ViewModel() {

    //데이터를 UI에 보여주기 위한 담당
    val productDataList: LiveData<List<ProductData>> = productDataRepository.getProductData()

    private var _clickProductData=MutableLiveData<ProductData>()

    private var _layoutHeight=MutableLiveData<Int>()
    //todo 서버에서 데이터를 가져왔을 경우를 생각해 레포지토리 생성하기

    val clickProductData: LiveData<ProductData>
        get() = _clickProductData

    val layoutHeight: LiveData<Int>
        get() = _layoutHeight


    init{
        loadProductData()
    }

    fun updateLayoutHeight(
        initialHeight: Int,
        initialTouchY: Float,
        currentTouchY: Float,
        screenHeight: Int
    ){
        val heightChange = (currentTouchY - initialTouchY).toInt()
        var newHeight = initialHeight - heightChange

        newHeight = newHeight.coerceAtLeast((screenHeight * MIN_HEIGHT_PERCENT).toInt())
        newHeight = newHeight.coerceAtMost((screenHeight * MAX_HEIGHT_PERCENT).toInt())

        _layoutHeight.value = newHeight
    }

    private  fun loadProductData(){
        productDataRepository.loadProductData()
    }

    fun loadClickProductData(productData: ProductData){
        _clickProductData.value=productData
        Log.d("_clickProductData",_clickProductData.value.toString())
    }

    companion object{
        const val MIN_HEIGHT_PERCENT=0.3
        const val MAX_HEIGHT_PERCENT=0.9
    }
}