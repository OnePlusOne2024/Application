package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.ProductCategoryType
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

    private var _filterProductData=MutableLiveData<List<ProductData>>()

    //todo 서버에서 데이터를 가져왔을 경우를 생각해 레포지토리 생성하기

    val clickProductData: LiveData<ProductData>
        get() = _clickProductData

    val layoutHeight: LiveData<Int>
        get() = _layoutHeight

    val filterProductData: LiveData<List<ProductData>>
        get() = _filterProductData

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

//    fun loadFilteringProductData(mainFilterData: List<MainFilterData>) {
//
//        val filteredProductList: MutableList<ProductData> = mutableListOf()
//
//        productDataList.value?.let { productList ->
//            for (product in productList) {
//                var matchesAllFilters = true
//
//                for (filter in mainFilterData) {
//                    Log.d("filter", filter.toString())
//                    val matchesCurrentFilter = when (filter.filterType) {
//                        FilterType.CONVENIENCE -> {
//                            filter.mainFilterText == "편의점 전체" || product.brand == filter.mainFilterText
//                        }
//                        FilterType.PRODUCT_CATEGORY -> {
//                            filter.mainFilterText == "모든 상품" || product.category == filter.mainFilterText
//                        }
//                        FilterType.BENEFITS -> {
//                            filter.mainFilterText == "행사 전체" || product.benefits == filter.mainFilterText
//                        }
//                        else -> true
//                    }
//                    Log.d("filter", matchesCurrentFilter.toString())
//                    if (!matchesCurrentFilter) {
//                        matchesAllFilters = false
//                        break
//                    }
//                }
//                if (matchesAllFilters) {
//                    filteredProductList.add(product)
//                }
//            }
//        }
//        Log.d("filteredProductList", filteredProductList.toString())
//    }

    //todo filter와 all 알아보기
    fun loadFilteringProductData(mainFilterData: List<MainFilterData>) {
        productDataList.value?.let { productList ->
            val filteredProductList = productList.filter { product ->
                mainFilterData.all { filter ->
                    when (filter.filterType) {
                        FilterType.CONVENIENCE ->
                            filter.mainFilterText == ConvenienceType.ALL_CONVENIENCE_STORE.title
                                    || product.brand == filter.mainFilterText

                        FilterType.PRODUCT_CATEGORY ->
                            filter.mainFilterText == ProductCategoryType.ALL_PRODUCT_CATEGORY.title
                                    || product.category == filter.mainFilterText

                        FilterType.BENEFITS ->
                            filter.mainFilterText == BenefitsType.ALL_BENEFITS.title
                                    || product.benefits == filter.mainFilterText

                        else -> true
                    }
                }
            }
            _filterProductData.value=filteredProductList
        }
    }
    companion object{
        const val MIN_HEIGHT_PERCENT=0.3
        const val MAX_HEIGHT_PERCENT=0.9
    }
}