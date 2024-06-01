package com.example.oneplusone.repository

import androidx.paging.PagingData
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.MainFilterData
import kotlinx.coroutines.flow.Flow


interface DataBaseRepository {
    suspend fun getAllFavoriteProducts(): List<FavoriteProductModel>

    fun getAllFavoriteProductByPaging(mainFilterDataList: List<MainFilterData>): Flow<PagingData<FavoriteProductModel>>

    suspend fun insertFavoriteProduct(productData: FavoriteProductModel)
    suspend fun deleteFavoriteProduct(productData: FavoriteProductModel)



    suspend fun deleteAllFavoriteProductList()
    suspend fun getSearchFavoriteProductList(searchText:String):List<FavoriteProductModel>

    fun getAllServerProductDataList(mainFilterDataList: List<MainFilterData>): Flow<PagingData<ProductData>>

    fun getAllProductDataByConvenienceType(convenienceType: String,mainFilterDataList: List<MainFilterData>):Flow<PagingData<ProductData>>

    suspend fun insertServerProductDataList(productDataList: List<ProductData>)
    suspend fun deleteServerProductDataList()

    suspend fun getProductNameList():List<String>

    fun getSearchProductList(searchText: String,mainFilterDataList: List<MainFilterData>):Flow<PagingData<ProductData>>


}