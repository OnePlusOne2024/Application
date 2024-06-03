package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.MainFilterData

import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataBaseViewModel@Inject constructor(

    private val dbRepository: DataBaseRepository

):ViewModel(){
    private val _favoriteProducts = MutableLiveData<List<FavoriteProductModel>>()
    private val _DBProductDataList = MutableLiveData<Flow<PagingData<ProductData>>>()
    private val _productNameList=MutableLiveData<List<String>>()


    private val _convenienceType=MutableLiveData<String>()

    private val _serverConnectProcessState=MutableLiveData<Boolean>()

    private val _isLoading = MutableLiveData<Boolean>()

    private val _favoriteProductByPaging= MutableLiveData<Flow<PagingData<FavoriteProductModel>>>()

    private val _searchText=MutableLiveData<String>()

    private val _mainFilterDataList=MutableLiveData<List<MainFilterData>>()

    val favoriteProductByPaging: LiveData<Flow<PagingData<FavoriteProductModel>>>
        get() = _favoriteProductByPaging
    val isLoading: LiveData<Boolean> get() = _isLoading

    val favoriteProducts: LiveData<List<FavoriteProductModel>>
        get()=_favoriteProducts

    val DBProductDataList:LiveData<Flow<PagingData<ProductData>>>
        get()=_DBProductDataList

    val productNameList:LiveData<List<String>>
        get()=_productNameList

    val serverConnectProcessState:LiveData<Boolean>
        get()=_serverConnectProcessState

    val convenienceType:LiveData<String>
        get()=_convenienceType

    val searchText:LiveData<String>
        get()=_searchText


    val mainFilterDataList: LiveData<List<MainFilterData>>
        get() = _mainFilterDataList

    val homeMergeData = MediatorLiveData<Pair<List<MainFilterData>?, Boolean?>>().apply {
        value = Pair(null, null)
    }

    val mapMergeData = MediatorLiveData<Pair<List<MainFilterData>?, String?>>().apply {
        value = Pair(null, null)
    }

    val searchMergeData = MediatorLiveData<Pair<List<MainFilterData>?, String?>>().apply {
        value = Pair(null, null)
    }

    init {
        homeMergeData.addSource(mainFilterDataList) { mainFilterDataList ->
            val serverConnectProcessState = homeMergeData.value?.second
            homeMergeData.value = Pair(mainFilterDataList, serverConnectProcessState)
        }

        homeMergeData.addSource(serverConnectProcessState) { serverConnectProcessState ->
            val mainFilterDataList = homeMergeData.value?.first
            homeMergeData.value = Pair(mainFilterDataList, serverConnectProcessState)
        }

        mapMergeData.addSource(mainFilterDataList) { mainFilterDataList ->
            val convenienceType = mapMergeData.value?.second
            mapMergeData.value = Pair(mainFilterDataList, convenienceType)
        }

        mapMergeData.addSource(convenienceType) { convenienceType ->
            val mainFilterDataList = mapMergeData.value?.first
            mapMergeData.value = Pair(mainFilterDataList, convenienceType)
        }

        searchMergeData.addSource(mainFilterDataList) { mainFilterDataList ->
            val searchText = searchMergeData.value?.second
            searchMergeData.value = Pair(mainFilterDataList, searchText)
        }

        searchMergeData.addSource(searchText) { searchText ->
            val mainFilterDataList = searchMergeData.value?.first
            searchMergeData.value = Pair(mainFilterDataList, searchText)
        }

    }



    fun loadFavoriteProducts() {
        viewModelScope.launch {

            val products = dbRepository.getAllFavoriteProducts()
            _favoriteProducts.value = products
        }
    }

    private fun deleteAllFavoriteProduct(){
        viewModelScope.launch {
            dbRepository.deleteAllFavoriteProductList()
        }
    }

    fun loadProductDataList() {

        _DBProductDataList.value=dbRepository.getAllServerProductDataList(mainFilterDataList.value!!)

    }

    fun loadProductDataByConvenienceType(){

        _convenienceType.value?.let { _DBProductDataList.value=dbRepository.getAllProductDataByConvenienceType(it,mainFilterDataList.value!!) }

    }

    fun loadFavoriteProductDataByPaging(){
        _favoriteProductByPaging.value=dbRepository.getAllFavoriteProductByPaging(mainFilterDataList.value!!)
        Log.d("_favoriteProductByPaging.value", _favoriteProductByPaging.value.toString())


    }




    fun setSearchText(searchText: String){
        _searchText.value=searchText
    }

    fun loadSearchProductDataByPaging(){
        _searchText.value?.let{
            _DBProductDataList.value=dbRepository.getSearchProductList(it,mainFilterDataList.value!!)
        }
    }

    fun setConvenienceType(convenienceType: String){
        _convenienceType.value=convenienceType
    }

    fun loadProductNameList(){
        viewModelScope.launch {

            val productsNameList = dbRepository.getProductNameList()
            _productNameList.value = productsNameList
        }
    }
    fun favoriteProductJudgment(product: ProductData){

        val favoriteProductModel = FavoriteProductModel(
            id = product.id!!.toInt(),
            productName=product.productName,
            productPrice=product.productPrice,
            brand=product.brand,
            benefits = product.benefits,
            productImage = product.productImage,
            favorite = product.favorite,
            category = product.category,
            pb=product.pb
        )

        if(product.favorite){
            insertFavoriteProduct(favoriteProductModel)
        }else{
            deleteFavoriteProduct(favoriteProductModel)
            loadFavoriteProductDataByPaging()
        }
    }


    private fun convertServerProductDataToDBData(serverProductDate:List<ServerProductData>): List<ProductData> {



        return serverProductDate.map { product ->

            val serverConvenienceType = when (product.convname) {
                "EMART" -> "이마트 24"
                "SEVENELEVEN" -> "세븐 일레븐"
                else -> product.convname
            }

            ProductData(
                id = null,
                productName = product.name,
                productPrice = product.price,
                brand = serverConvenienceType,
                benefits = product.event,
                productImage = product.image,
                favorite = false,
                category = product.category,
                pb = product.pb
            )
        }
    }
    private fun insertFavoriteProduct(favoriteProductModel: FavoriteProductModel) {
        viewModelScope.launch {
            dbRepository.insertFavoriteProduct(favoriteProductModel)
        }
    }

    private fun deleteFavoriteProduct(favoriteProductModel: FavoriteProductModel) {
        viewModelScope.launch {
            dbRepository.deleteFavoriteProduct(favoriteProductModel)
        }
    }

    fun updateProductDatabase(productDataList: List<ServerProductData>) {
        viewModelScope.launch {

            deleteAllFavoriteProduct()

            deleteAllDBProductList()

            insertServerProductDataList(productDataList)


            _serverConnectProcessState.value=true
        }
    }

    private suspend fun insertServerProductDataList(productDataList: List<ServerProductData>) {
        val convertResult = convertServerProductDataToDBData(productDataList)
        dbRepository.insertServerProductDataList(convertResult)

    }

//지금 제대로 삭제후 데이터를 저장하지 못하고 있음 이를 해결해야함 반드시 서버로부터 데이터가 들어오면
    //상품db의 모든데이터를 제거한후 새로운데이터를 넣고 페이징 데이터를 로드해야함(해결)
    private suspend fun deleteAllDBProductList() {
        dbRepository.deleteServerProductDataList()
    }

    fun waitServerConnectProcess(serverConnectProcessState:Boolean){
        _serverConnectProcessState.value=serverConnectProcessState
    }

    fun setCurrentMainFilterData(mainFilterDataList: List<MainFilterData>) {
        _mainFilterDataList.value=mainFilterDataList
    }



}