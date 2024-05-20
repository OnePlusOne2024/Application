package com.example.oneplusone.repository

import com.example.oneplusone.db.FavoriteProductDao
import com.example.oneplusone.db.FavoriteProductModel

class FavoriteProductRepositoryImpl(private val favoriteProductDao: FavoriteProductDao) : FavoriteProductRepository {

    override suspend fun getAllFavoriteProducts(): List<FavoriteProductModel> {
        return favoriteProductDao.getAllFavoriteProduct()
    }

    override suspend fun insertFavoriteProduct(productData: FavoriteProductModel) {
        favoriteProductDao.insertFavoriteProduct(productData)
    }

    override suspend fun deleteFavoriteProduct(productData: FavoriteProductModel) {
        favoriteProductDao.deleteFavoriteProduct(productData)
    }
}