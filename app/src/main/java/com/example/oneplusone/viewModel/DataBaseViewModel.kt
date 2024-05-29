package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData

import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    init {
//        loadFavoriteProducts()
//        loadProductDataList()
        //db의 상품정보 데이터를 모두 지움
//        deleteAllDBProductList()
    }



    fun loadFavoriteProducts() {
        viewModelScope.launch {

            val products = dbRepository.getAllFavoriteProducts()
            _favoriteProducts.value = products
        }
    }

    fun deleteAllFavoriteProduct(){
        viewModelScope.launch {
            dbRepository.deleteAllFavoriteProductList()
        }
    }

    fun loadProductDataList() {

        _DBProductDataList.value=dbRepository.getAllServerProductDataList()

    }

    fun loadProductDataByConvenienceType(){

        _convenienceType.value?.let { _DBProductDataList.value=dbRepository.getAllProductDataByConvenienceType(it) }

    }

    fun loadFavoriteProductDataByPaging(){
        _favoriteProductByPaging.value=dbRepository.getAllFavoriteProductByPaging()
    }



    fun loadSearchFavoriteProducts(newSearchText: String) {
        viewModelScope.launch {

            val products = dbRepository.getSearchFavoriteProductList(newSearchText)
            Log.d("loadSearchFavoriteProducts", products.toString())
            _favoriteProducts.value = products
        }
    }

    fun loadSearchProductDataList(newSearchText: String) {
//        viewModelScope.launch {
//
//            val productsDataList = dbRepository.getSearchProductList(newSearchText)
//            Log.d("loadSearchFavoriteProducts", productsDataList.toString())
//            _DBProductDataList.value= productsDataList
//        }
    }

    fun setSearchText(searchText: String){
        _searchText.value=searchText
    }

    fun loadSearchProductDataByPaging(){
        _searchText.value?.let{
            _DBProductDataList.value=dbRepository.getSearchProductList(it)
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
    //상품db의 모든데이터를 제거한후 새로운데이터를 넣고 페이징 데이터를 로드해야함
    private suspend fun deleteAllDBProductList() {
        dbRepository.deleteServerProductDataList()
    }

    fun waitServerConnectProcess(serverConnectProcessState:Boolean){
        _serverConnectProcessState.value=serverConnectProcessState
    }



}