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
import com.example.oneplusone.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

//    val favoritePagingBooks: StateFlow<PagingData<ProductData>> =
//        dbRepository.getAllServerProductDataList()
//            .cachedIn(viewModelScope) // 코루틴이 데이터흐름을 캐시하고 공유 가능하게 만든다.
//            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
//

    private val _serverConnectProcessState=MutableLiveData<Boolean>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val favoriteProducts: LiveData<List<FavoriteProductModel>>
        get()=_favoriteProducts

    val DBProductDataList:LiveData<Flow<PagingData<ProductData>>>
        get()=_DBProductDataList

    val productNameList:LiveData<List<String>>
        get()=_productNameList

    val serverConnectProcessState:LiveData<Boolean>
        get()=_serverConnectProcessState
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

    fun loadProductDataList() {

        _DBProductDataList.value=dbRepository.getAllServerProductDataList()

    }

    fun toggleLoadingBar(loadingValue: Boolean) {
        _isLoading.value=loadingValue
//        _isLoading.value=!_isLoading.value!!
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

    fun insertServerProductDataList(productDataList:List<ServerProductData>){
        val convertResult=convertServerProductDataToDBData(productDataList)

        viewModelScope.launch {
            dbRepository.insertServerProductDataList(convertResult)
            waitServerConnectProcess(true)
        }
    }

    fun deleteAllDBProductList(){
        viewModelScope.launch {
            dbRepository.deleteServerProductDataList()
        }
    }

    fun waitServerConnectProcess(serverConnectProcessState:Boolean){
        _serverConnectProcessState.value=serverConnectProcessState
    }
}