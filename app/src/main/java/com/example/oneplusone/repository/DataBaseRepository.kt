package com.example.oneplusone.repository

import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData


interface DataBaseRepository {
    suspend fun getAllFavoriteProducts(): List<FavoriteProductModel>
    suspend fun insertFavoriteProduct(productData: FavoriteProductModel)
    suspend fun deleteFavoriteProduct(productData: FavoriteProductModel)
    suspend fun getSearchFavoriteProductList(searchText:String):List<FavoriteProductModel>

    suspend fun getAllServerProductDataList():List<ProductData>
    suspend fun insertServerProductDataList(productDataList: List<ProductData>)
    suspend fun deleteServerProductDataList()

    suspend fun getProductNameList():List<String>

    suspend fun getSearchProductList(searchText:String):List<ProductData>
}