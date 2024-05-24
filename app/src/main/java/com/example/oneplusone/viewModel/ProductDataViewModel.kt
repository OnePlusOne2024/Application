package com.example.oneplusone.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.MainFilterData
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


    private val _favoriteProductData=MutableLiveData<List<ProductData>?>()

    private val _connectTime=MutableLiveData<String?>()

    private val _updateCheckResult=MutableLiveData<Boolean?>()

    private val _serverProductDataList=MutableLiveData<List<ServerProductData>?>()

    private val _DBProductDataList=MutableLiveData<List<ProductData>>()

    private val _mainFilterDataList=MutableLiveData<List<MainFilterData>>()

    //MediatorLiveData를 사용해 여러개의 라이브데이터를 하나로 합침
    val _mergeData = MediatorLiveData<Pair<List<ProductData>?, List<ProductData>?>>().apply {
        value = Pair(null, null)
    }

    val isFavorite: LiveData<ProductData>
        get()=_isFavorite

    val productDataList:LiveData<List<ProductData>?>
        get()=_productDataList

    //todo 서버에서 데이터를 가져왔을 경우를 생각해 레포지토리 생성하기

    val clickProductData: LiveData<ProductData>
        get() = _clickProductData

    val mainFilterDataList: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList

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

    val serverProductDataList: LiveData<List<ServerProductData>?>
        get() = _serverProductDataList

    val favoriteProductData:LiveData<List<ProductData>?>
        get() = _favoriteProductData
    val DBProductDataList:LiveData<List<ProductData>>
        get() = _DBProductDataList
    fun toggleFavorite(productData: ProductData) {

        productData.favorite = !productData.favorite

        _isFavorite.value=productData

    }
    fun favoriteProductJudgment(productData: ProductData){

        if(!productData.favorite){
            _productDataList.value = _productDataList.value?.filter { it != productData }
        }
    }
    init {

        //좋아요한 상품목록, 일반상품목록 모두 db에서 가져와 비교해야 하는데 두 데이터가 모두 준비됐을때만 productData의 값을
        //변경해야 하기 때문에 MediatorLiveData를 사용했음남기다
        _mergeData.addSource(favoriteProductData) { favoriteProducts ->
            val dbProducts = _mergeData.value?.second
            _mergeData.value = Pair(favoriteProducts, dbProducts)
        }

        _mergeData.addSource(DBProductDataList) { dbProductDataList ->
            val favoriteProducts = _mergeData.value?.first
            _mergeData.value = Pair(favoriteProducts, dbProductDataList)
        }

    }


    init{

//        loadProductData()
        //아직 서버에서 업데이트 체크 기능을 만들지 않았기 때문에 한번만 실행함
//        getProductDataFromServer()
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

    fun loadProductData(){

        //처음 데이터를 불러와 화면에 바로 출력하는 대신에 db의 데이터와 대조하여 즐겨찾기한 아이템과 비교 후 화면에 출력,
        val DBProductData=_DBProductDataList.value
        val DBfavoriteProductData=_favoriteProductData.value

        val updatedList = DBProductData?.map { originalProduct ->
            DBfavoriteProductData?.find { it.id == originalProduct.id } ?: originalProduct
        }
        _productDataList.value=updatedList

        if(_mainFilterDataList.value!=null){
            loadFilteredProductData(_mainFilterDataList.value!!)
        }
    }


    fun loadDBProductData(productDataList:List<ProductData>){
        _DBProductDataList.value=productDataList

        Log.d("_DBProductDataList.value", _DBProductDataList.value.toString())
    }

    fun loadFavoriteProduct(favoriteProduct: List<FavoriteProductModel>){

        val convertProductDataList=convertProductDataType(favoriteProduct)

        _favoriteProductData.value=convertProductDataList
    }

    fun loadFavoriteProductInFavoriteProductFragment(favoriteProduct: List<FavoriteProductModel>){
        val convertProductDataList=convertProductDataType(favoriteProduct)
        _productDataList.value=convertProductDataList
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



    fun loadClickProductData(productData: ProductData){
        _clickProductData.value=productData
    }



    //todo filter와 all 알아보기
    fun loadFilteredProductData(mainFilterData: List<MainFilterData>) {
        Log.d("mainFilterData", mainFilterData.toString())
        _productDataList.value?.let { productList ->

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
        Log.d("productDataList", productDataList.value.toString())

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



    fun updateCheckResult(connectTime:String?){
        productDataRepository.getUpdateInfoCheck(connectTime) { updateCheckResult ->

            _updateCheckResult.value=updateCheckResult

        }
//        getProductDataFromServer()
    }

    //서버에서 상품 목록을 가져옴
    fun getProductDataFromServer(){
        Log.d("실행됨", "serverProductData.toString()")

        productDataRepository.getProductDataList { serverProductData ->
            if (serverProductData != null) {
                _serverProductDataList.value=serverProductData
            }
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

    fun setMainFilterData(mainFilterDataList: List<MainFilterData>) {
        _mainFilterDataList.value=mainFilterDataList
    }

    companion object{
        const val MIN_HEIGHT_PERCENT=0.3
        const val MAX_HEIGHT_PERCENT=0.9
    }
}