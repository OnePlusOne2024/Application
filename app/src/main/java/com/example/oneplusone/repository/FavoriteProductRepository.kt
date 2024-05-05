package com.example.oneplusone.repository

import com.example.oneplusone.db.FavoriteProductModel

interface FavoriteProductRepository {
    suspend fun getAllFavoriteProducts(): List<FavoriteProductModel>
    suspend fun insertFavoriteProduct(productData: FavoriteProductModel)
    suspend fun deleteFavoriteProduct(productData: FavoriteProductModel)
}