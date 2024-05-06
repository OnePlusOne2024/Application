package com.example.oneplusone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.repository.FavoriteProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataBaseViewModel@Inject constructor(

    private val dbRepository: FavoriteProductRepository

):ViewModel(){
    private val _favoriteProducts = MutableLiveData<List<FavoriteProductModel>>()


    val favoriteProducts: LiveData<List<FavoriteProductModel>>
        get()=_favoriteProducts


    init {
        loadFavoriteProducts()
    }

    private fun loadFavoriteProducts() {
        viewModelScope.launch {

            val products = dbRepository.getAllFavoriteProducts()
            _favoriteProducts.value = products
        }
    }

    fun favoriteProductJudgment(product: ProductData){

        val favoriteProductModel = FavoriteProductModel(
            id = product.id,
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

}