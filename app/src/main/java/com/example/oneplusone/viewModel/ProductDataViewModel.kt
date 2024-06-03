package com.example.oneplusone.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ServerResponse
import com.example.oneplusone.repository.ProductDataRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


//productDataRepository : ProductDataRepository

@HiltViewModel
class ProductDataViewModel @Inject constructor(
    //소스를 가져오거나 저장하는 담당
    private val productDataRepository: ProductDataRepository

):ViewModel() {

    //데이터를 UI에 보여주기 위한 담당
    private val _productDataList= MutableLiveData<ProductData?>()

    private var _clickProductData=MutableLiveData<ProductData>()

    private var _layoutHeight=MutableLiveData<Int>()

    private val _isFavorite = MutableLiveData<ProductData>()

    private val _productNameList=MutableLiveData<ArrayList<String>>()


    private val _favoriteProductData=MutableLiveData<List<ProductData>?>()


    private val _serverProductDataList=MutableLiveData<ServerResponse?>()

    private val _DBProductDataList=MutableLiveData<ProductData>()

    private val _mainFilterDataList=MutableLiveData<List<MainFilterData>>()

    private val _convenienceType=MutableLiveData<String>()

    private val _userCoordinate=MutableLiveData<LatLng>()



    private val _notifyMonth= MutableLiveData<String>()


    val userCoordinate: LiveData<LatLng>
        get()=_userCoordinate
    val isFavorite: LiveData<ProductData>
        get()=_isFavorite




    val convenienceType: LiveData<String>
        get() = _convenienceType

    val notifyMonth: LiveData<String>
        get() = _notifyMonth

    val clickProductData: LiveData<ProductData>
        get() = _clickProductData

    val mainFilterDataList: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList

    val layoutHeight: LiveData<Int>
        get() = _layoutHeight


    val productNameList: LiveData<ArrayList<String>>
        get() = _productNameList


    val serverProductDataList: LiveData<ServerResponse?>
        get() = _serverProductDataList

    init{
        notifyMonth()
    }

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

    fun setCoordinate(latitude: Double, longitude: Double){
        _userCoordinate.value= LatLng(latitude,longitude)
    }

    @SuppressLint("SimpleDateFormat")
    fun notifyMonth(){

        val simpleDateFormat = SimpleDateFormat("M")
        val date = Date()
        _notifyMonth.value = simpleDateFormat.format(date)+ NOTIFY_MONTH_EVENT_MESSAGE

    }


    //서버에서 상품 목록을 가져옴
    fun getProductDataFromServer(loadConnectTime: String?) {
        Log.d("실행됨", "serverProductData.toString()")

        productDataRepository.getProductDataList(loadConnectTime) { serverProductData ->
            Log.d("serverProductData", serverProductData.toString())

            _serverProductDataList.value=serverProductData

        }
    }


    fun isProductFavorite(productData: ProductData): ProductData {
        val favoriteProducts = _favoriteProductData.value
        val updatedList=favoriteProducts!!.find{
            it.id== productData.id
        }?:productData

        return updatedList
    }


    fun loadFavoriteProduct(favoriteProduct: List<FavoriteProductModel>){

        val convertProductDataList=convertProductDataType(favoriteProduct)

        _favoriteProductData.value=convertProductDataList
    }



    private fun convertProductDataType(favoriteProduct: List<FavoriteProductModel>): List<ProductData> {
        return favoriteProduct.map { product ->
            ProductData(
                id = product.id.toLong(),
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

    fun convertSingleProductDataType(favoriteProduct: FavoriteProductModel): ProductData {
        return ProductData(
                id = favoriteProduct.id.toLong(),
                productName = favoriteProduct.productName,
                productPrice = favoriteProduct.productPrice,
                brand = favoriteProduct.brand,
                benefits = favoriteProduct.benefits,
                productImage = favoriteProduct.productImage,
                favorite = favoriteProduct.favorite,
                category = favoriteProduct.category,
                pb = favoriteProduct.pb
            )
        }


    fun loadClickProductData(productData: ProductData){
        _clickProductData.value=productData
    }




    companion object{
        const val MIN_HEIGHT_PERCENT=0.3
        const val MAX_HEIGHT_PERCENT=0.9

        const val NOTIFY_MONTH_EVENT_MESSAGE=" 월의 행사 상품 입니다."
    }
}