package com.example.oneplusone.repository

import com.example.oneplusone.db.FavoriteProductDao
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductDao
import com.example.oneplusone.db.ProductModel
import com.example.oneplusone.model.data.ProductData

class DataBaseRepositoryImpl(
    private val favoriteProductDao: FavoriteProductDao,
    private val serverProductDao: ProductDao
) : DataBaseRepository {

    override suspend fun getAllFavoriteProducts(): List<FavoriteProductModel> {
        return favoriteProductDao.getAllFavoriteProduct()
    }

    override suspend fun insertFavoriteProduct(productData: FavoriteProductModel) {
        favoriteProductDao.insertFavoriteProduct(productData)
    }

    override suspend fun deleteFavoriteProduct(productData: FavoriteProductModel) {
        favoriteProductDao.deleteFavoriteProduct(productData)
    }

    override suspend fun getAllServerProductDataList(): List<ProductModel> {
        return serverProductDao.getAllProductData()
    }

    override suspend fun insertServerProductDataList(productDataList: List<ProductModel>) {
        serverProductDao.insertProductData(productDataList)
    }

    override suspend fun deleteServerProductDataList() {
        serverProductDao.deleteAllProductData()
    }
}