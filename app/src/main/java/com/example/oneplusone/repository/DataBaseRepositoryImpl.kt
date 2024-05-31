package com.example.oneplusone.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.oneplusone.db.FavoriteProductDao
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductDao
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.util.FavoriteProductDataPagingSource
import com.example.oneplusone.util.ProductDataPagingSource
import kotlinx.coroutines.flow.Flow


class DataBaseRepositoryImpl(
    private val favoriteProductDao: FavoriteProductDao,
    private val serverProductDao: ProductDao
) : DataBaseRepository {

    override suspend fun getAllFavoriteProducts(): List<FavoriteProductModel> {
        return favoriteProductDao.getAllFavoriteProduct()
    }

    override fun getAllFavoriteProductByPaging(): Flow<PagingData<FavoriteProductModel>>{

        return Pager(
            config =  PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FavoriteProductDataPagingSource(favoriteProductDao) }
        ).flow

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

    override suspend fun deleteAllFavoriteProductList(){
        favoriteProductDao.deleteAllFavoriteProduct()
    }

    override fun getAllServerProductDataList(mainFilterDataList: List<MainFilterData>): Flow<PagingData<ProductData>> {


        return Pager(
            config =  PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductDataPagingSource(serverProductDao,null,null,mainFilterDataList) }
        ).flow
    }

    override fun getAllProductDataByConvenienceType(convenienceType: String,mainFilterDataList: List<MainFilterData>):Flow<PagingData<ProductData>>{
        Log.d("convenienceType2",convenienceType)
        val pagingSourceFactory ={ ProductDataPagingSource(
            serverProductDao,
            convenienceType,
            null,
            mainFilterDataList
        )}

        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false,

            ),
            pagingSourceFactory =  pagingSourceFactory
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

    override fun getSearchProductList(searchText: String,mainFilterDataList: List<MainFilterData>): Flow<PagingData<ProductData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false,

                ),
            pagingSourceFactory =  { ProductDataPagingSource(
                serverProductDao,
                null,
                searchText,
                mainFilterDataList
            ) }
        ).flow
    }
}