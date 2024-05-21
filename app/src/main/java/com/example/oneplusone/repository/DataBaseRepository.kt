package com.example.oneplusone.repository

import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductModel
import com.example.oneplusone.model.data.ProductData

interface DataBaseRepository {
    suspend fun getAllFavoriteProducts(): List<FavoriteProductModel>
    suspend fun insertFavoriteProduct(productData: FavoriteProductModel)
    suspend fun deleteFavoriteProduct(productData: FavoriteProductModel)

    suspend fun getAllServerProductDataList():List<ProductModel>
    suspend fun insertServerProductDataList(productDataList: List<ProductModel>)
    suspend fun deleteServerProductDataList()
}