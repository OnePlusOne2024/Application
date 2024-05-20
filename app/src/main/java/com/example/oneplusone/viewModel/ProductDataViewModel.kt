package com.example.oneplusone.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.ProductCategoryType
import com.example.oneplusone.repository.ProductDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
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

    private val _productNameList=MutableLiveData<ArrayList<String>>()


    private val _favoriteProductData=MutableLiveData<List<FavoriteProductModel>?>()

    private val _connectTime=MutableLiveData<String?>()

    private val _updateCheckResult=MutableLiveData<Boolean?>()
    val isFavorite: LiveData<ProductData>
        get()=_isFavorite

    val productDataList:LiveData<List<ProductData>?>
        get()=_productDataList

    //todo 서버에서 데이터를 가져왔을 경우를 생각해 레포지토리 생성하기

    val clickProductData: LiveData<ProductData>
        get() = _clickProductData

    val layoutHeight: LiveData<Int>
        get() = _layoutHeight

    val filterProductData: LiveData<List<ProductData>>
        get() = _filterProductData

    val convenienceProductData: LiveData<List<ProductData>>
        get() = _convenienceProductData

    val productNameList: LiveData<ArrayList<String>>
        get() = _productNameList

    val connectTime: LiveData<String?>
        get()=_connectTime

    val updateCheckResult: LiveData<Boolean?>
        get()=_updateCheckResult
    fun toggleFavorite(productData: ProductData) {

        productData.favorite = !productData.favorite

        _isFavorite.value=productData

    }

    init{
        Log.d("처음실행","실행")
        getProductDataFromServer()
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

    fun loadProductData(favoriteProduct: List<FavoriteProductModel>?=null,searchText: String?=null){

        _favoriteProductData.value=favoriteProduct
        //처음 데이터를 불러와 화면에 바로 출력하는 대신에 db의 데이터와 대조하여 즐겨찾기한 아이템과 비교 후 화면에 출력,
        val initProductData=productDataRepository.loadProductData()

        val convertProductDataList= _favoriteProductData.value?.let { convertProductDataType(it) }

        //서치텍스트가 있다면 서치결과를 필터링하고 아니라면 그냥 받음
        val searchResult=searchText?.let{
            loadSearchProductList(initProductData,searchText)
        }?:initProductData

        val updatedList = searchResult.map { originalProduct ->
            convertProductDataList?.find { it.id == originalProduct.id } ?: originalProduct
        }
        _productDataList.value=updatedList
        //검색에 사용하기 위해 상품 목록의 이름만 따로 분리하여 저장
        loadProductNameList(initProductData)
    }

    //서버에서 받아온 데이터를 db에 저장해야함
    fun saveProductData(){

    }

    fun loadFavoriteProduct(favoriteProduct: List<FavoriteProductModel>){

        val convertProductDataList=convertProductDataType(favoriteProduct)

        _productDataList.value=convertProductDataList
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

    //ignoreCase = true면 대소문자 구분 안하는거
    private fun loadSearchProductList(initProductData: List<ProductData>, searchText: String):List<ProductData> {
        val filterProductData = initProductData.filter  { originalProduct ->
            originalProduct.productName.contains(searchText, ignoreCase = true)
        }
        return filterProductData
    }
//    fun setSearchText(searchText: String){
//
//        _searchTextCheckResult.value=SearchTextCheck().searchTextCheck(searchText)
//        if(_searchTextCheckResult.value==true){
//            //일단 새로 불러오도록 했음 근데 이게 맞는방법인지는 다시 알아봐야함
//            this._searchText.value=searchText
//            favoriteProductCheck()
//        }
//    }


//    private  fun loadProductData(): List<ProductData>{
//        return productDataRepository.loadProductData()
//    }
    private fun loadProductNameList(initProductData: List<ProductData>) {
        _productNameList.value=ArrayList(initProductData.map { it.productName })
    }


    fun loadConnectTime(context: Context){
        val sharedPreferences = context.getSharedPreferences("LastConnectTime", Context.MODE_PRIVATE)
        val lastConnectTime = sharedPreferences.getString("lastConnectTime",null)
        val editor = sharedPreferences.edit()

        //접속했으면 현재 날짜를 등록
        editor.putString("lastConnectTime", getCurrentDate().toString())
        editor.apply()
        //서버와 통신해서 데이터를 가져와야함, 만약 null이라면 처음 접속한것이기 때문에 다가져와야함
        if (lastConnectTime != null) {
            Log.d("lastConnectTime",lastConnectTime)
        }
        _connectTime.value=lastConnectTime

    }

    fun updateCheckResult(connectTime:Int?){
        productDataRepository.getUpdateInfoCheck(connectTime.toString()) { updateCheckResult ->
            if (updateCheckResult != null) {
                _updateCheckResult.value=updateCheckResult

            } else {

            }
        }
    }

    private fun getProductDataFromServer(){
        Log.d("실행됨", "serverProductData.toString()")

        productDataRepository.getProductDataList { serverProductData ->
            if (serverProductData != null) {
                val convertResult=convertServerProductData(serverProductData)
                Log.d("convertResult", convertResult.toString())
            }
        }
    }

    private fun convertServerProductData(serverProductDate:List<ServerProductData>): List<ProductData> {
        return serverProductDate.map { product ->
            ProductData(
                id = null,
                productName = product.name,
                productPrice = product.price,
                brand = product.convname,
                benefits = product.event,
                productImage = product.image,
                favorite = false,
                category = product.category,
                pb = product.pb
            )
        }
    }

    //todo 최근 검색어 삭제하는 로직 구현하기

//    fun saveConnectTime(context: Context){
//        val sharedPreferences = context.getSharedPreferences("LastConnectTime", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        val currentDate=getCurrentDate()
//
//        editor.putString("lastConnectTime", currentDate)
//        editor.apply()
//
//    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): LocalDateTime {

        return LocalDateTime.now()
    }

    companion object{
        const val MIN_HEIGHT_PERCENT=0.3
        const val MAX_HEIGHT_PERCENT=0.9
    }
}