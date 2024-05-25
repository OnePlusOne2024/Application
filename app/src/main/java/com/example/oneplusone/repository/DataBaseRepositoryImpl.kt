package com.example.oneplusone.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.oneplusone.db.FavoriteProductDao
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductDao
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.util.ProductDataPagingSource
import kotlinx.coroutines.flow.Flow


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

    override suspend fun getSearchFavoriteProductList(searchText: String): List<FavoriteProductModel> {
        return favoriteProductDao.getSearchProduct(searchText)
    }

    override fun getAllServerProductDataList(): Flow<PagingData<ProductData>> {


        return Pager(
            config =  PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductDataPagingSource(serverProductDao) }
        ).flow
    }

    override suspend fun insertServerProductDataList(productDataList: List<ProductData>) {
        serverProductDao.insertProductData(productDataList)
    }

    override suspend fun deleteServerProductDataList() {
        serverProductDao.deleteAllProductData()
    }

    override suspend fun getProductNameList(): List<String> {
        return serverProductDao.getProductNameList()
    }

    override suspend fun getSearchProductList(searchText: String): List<ProductData> {
        return serverProductDao.getSearchProduct(searchText)
    }
}