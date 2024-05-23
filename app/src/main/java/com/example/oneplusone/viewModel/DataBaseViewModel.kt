package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData

import com.example.oneplusone.model.data.ServerProductData
import com.example.oneplusone.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataBaseViewModel@Inject constructor(

    private val dbRepository: DataBaseRepository

):ViewModel(){
    private val _favoriteProducts = MutableLiveData<List<FavoriteProductModel>>()
    private val _DBProductDataList = MutableLiveData<List<ProductData>>()
    private val _productNameList=MutableLiveData<List<String>>()

    val favoriteProducts: LiveData<List<FavoriteProductModel>>
        get()=_favoriteProducts

    val DBProductDataList:LiveData<List<ProductData>>
        get()=_DBProductDataList

    val productNameList:LiveData<List<String>>
        get()=_productNameList
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
        viewModelScope.launch {

            val productsDataList = dbRepository.getAllServerProductDataList()
            _DBProductDataList.value= productsDataList
        }
    }

    fun loadSearchFavoriteProducts(newSearchText: String) {
        viewModelScope.launch {

            val products = dbRepository.getSearchFavoriteProductList(newSearchText)
            Log.d("loadSearchFavoriteProducts", products.toString())
            _favoriteProducts.value = products
        }
    }

    fun loadSearchProductDataList(newSearchText: String) {
        viewModelScope.launch {

            val productsDataList = dbRepository.getSearchProductList(newSearchText)
            Log.d("loadSearchFavoriteProducts", productsDataList.toString())
            _DBProductDataList.value= productsDataList
        }
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

        Log.d("convertResult", convertResult.toString())
        viewModelScope.launch {
            dbRepository.insertServerProductDataList(convertResult)
        }
    }

    fun deleteAllDBProductList(){
        viewModelScope.launch {
            dbRepository.deleteServerProductDataList()
        }
    }
}