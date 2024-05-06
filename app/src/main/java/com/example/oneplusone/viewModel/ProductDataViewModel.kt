package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.model.data.ConvenienceData
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
    private val _productDataList= MutableLiveData<List<ProductData>?>()

    private var _clickProductData=MutableLiveData<ProductData>()

    private var _layoutHeight=MutableLiveData<Int>()

    private var _filterProductData=MutableLiveData<List<ProductData>>()

    private var _convenienceProductData=MutableLiveData<List<ProductData>>()


    private val _isFavorite = MutableLiveData<ProductData>()

    private val _productData = MutableLiveData<List<ProductData>>()

    val isFavorite: LiveData<ProductData>
        get()=_isFavorite

    val productDataList:LiveData<List<ProductData>?>
        get()=_productDataList

    val productData: LiveData<List<ProductData>>
        get() = _productData
    //todo 서버에서 데이터를 가져왔을 경우를 생각해 레포지토리 생성하기

    val clickProductData: LiveData<ProductData>
        get() = _clickProductData

    val layoutHeight: LiveData<Int>
        get() = _layoutHeight

    val filterProductData: LiveData<List<ProductData>>
        get() = _filterProductData

    val convenienceProductData: LiveData<List<ProductData>>
        get() = _convenienceProductData


//    init{
//
//        loadProductData()
//    }

//    fun updateProductFavorite(productData: ProductData){
//        productData.favorite=!productData.favorite
//        _isFavorite.value=productData
//    }

    fun toggleFavorite(productData: ProductData) {

        productData.favorite = !productData.favorite

        _isFavorite.value=productData

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

    fun favoriteProductCheck(favoriteProduct: List<FavoriteProductModel>){

        //처음 데이터를 불러와 화면에 바로 출력하는 대신에 db의 데이터와 대조하여 즐겨찾기한 아이템과 비교 후 화면에 출력
        val initProductData=productDataRepository.loadProductData()

        val convertProductDataList=convertProductDataType(favoriteProduct)

        val updatedList = initProductData.map { originalProduct ->
            convertProductDataList.find { it.id == originalProduct.id } ?: originalProduct
        }
        _productDataList.value=updatedList
    }

    private fun convertProductDataType(favoriteProduct: List<FavoriteProductModel>): List<ProductData> {
        return favoriteProduct.map { product ->
            ProductData(
                id = product.id!!,
                productName = product.productName,
                productPrice = product.productPrice,
                brand = product.brand,
                benefits = product.benefits,
                productImage = product.productImage,
                favorite = product.favorite,
                category = product.category,
                pb = product.pb
            )
        }
    }

//    private  fun loadProductData(){
//        _productDataList.value=productDataRepository.loadProductData()
//    }

    fun loadClickProductData(productData: ProductData){
        _clickProductData.value=productData
    }



    //todo filter와 all 알아보기
    fun loadFilteredProductData(mainFilterData: List<MainFilterData>) {
        Log.d("mainFilterData", mainFilterData.toString())
        productDataList.value?.let { productList ->

            // PB 필터를 적용한 결과에 메인 필터 적용
            val finalFilteredProductList = productList.filter { product ->
                mainFilterData.all { filter ->
                    when (filter.filterType) {
                        FilterType.CONVENIENCE ->
                            filter.mainFilterText == ConvenienceType.ALL_CONVENIENCE_STORE.title || product.brand == filter.mainFilterText
                        FilterType.PRODUCT_CATEGORY ->
                            filter.mainFilterText == ProductCategoryType.ALL_PRODUCT_CATEGORY.title || product.category == filter.mainFilterText
                        FilterType.BENEFITS ->
                            filter.mainFilterText == BenefitsType.ALL_BENEFITS.title || product.benefits == filter.mainFilterText
                        FilterType.PB ->
                            if (filter.mainFilterText == "PB 상품만") {
                                product.pb
                            } else {
                                true
                            }
                        else -> true
                    }
                }
            }
            Log.d("finalFilteredProductList", finalFilteredProductList.toString())
            _filterProductData.value = finalFilteredProductList
        }
    }

    fun loadConvenienceProductData(selectedMarkerData: ConvenienceData) {
        Log.d("selectedMarkerData", selectedMarkerData.toString())
        productDataList.value?.let { productList ->

            val convenienceFilteredProductList = productList.filter { product ->
                when (selectedMarkerData.convenienceType) {
                    ConvenienceType.STORE_CU.title -> product.brand == ConvenienceType.STORE_CU.title
                    ConvenienceType.STORE_SEVEN_ELEVEN.title -> product.brand == ConvenienceType.STORE_SEVEN_ELEVEN.title
                    ConvenienceType.STORE_GS_25.title -> product.brand == ConvenienceType.STORE_GS_25.title
                    ConvenienceType.STORE_E_MART24.title -> product.brand == ConvenienceType.STORE_E_MART24.title
                    else -> true
                }
            }

            _convenienceProductData.value = convenienceFilteredProductList
        }
    }

    fun loadMapFilteredProductData(mainFilterData: List<MainFilterData>) {
        Log.d("mainFilterData", mainFilterData.toString())
        _convenienceProductData.value?.let { productList ->

            // PB 필터를 적용한 결과에 메인 필터 적용
            val finalFilteredProductList = productList.filter { product ->
                mainFilterData.all { filter ->
                    when (filter.filterType) {
                        FilterType.CONVENIENCE ->
                            filter.mainFilterText == ConvenienceType.ALL_CONVENIENCE_STORE.title || product.brand == filter.mainFilterText
                        FilterType.PRODUCT_CATEGORY ->
                            filter.mainFilterText == ProductCategoryType.ALL_PRODUCT_CATEGORY.title || product.category == filter.mainFilterText
                        FilterType.BENEFITS ->
                            filter.mainFilterText == BenefitsType.ALL_BENEFITS.title || product.benefits == filter.mainFilterText
                        FilterType.PB ->
                            if (filter.mainFilterText == "PB 상품만") {
                                product.pb
                            } else {
                                true
                            }
                        else -> true
                    }
                }
            }
            Log.d("finalFilteredProductList", finalFilteredProductList.toString())
            _filterProductData.value = finalFilteredProductList
        }
    }

    companion object{
        const val MIN_HEIGHT_PERCENT=0.3
        const val MAX_HEIGHT_PERCENT=0.9
    }
}