package com.example.oneplusone.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.oneplusone.model.data.enums.ConvenienceType

@Dao
interface ProductDao {

    @Query("SELECT * FROM productData ORDER BY id ASC LIMIT 50 OFFSET (:page-1)*50")
    suspend fun getAllProductData(page:Int): List<ProductData>

    @Query("SELECT * FROM productData WHERE brand=:convenienceType LIMIT 50 OFFSET (:page-1)*50")
    suspend fun getAllProductDataByConvenienceType(page:Int,convenienceType: String): List<ProductData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductData(productData: List<ProductData>)

    @Query("DELETE FROM productData")
    suspend fun deleteAllProductData()

    @Query("SELECT productName FROM productData")
    suspend fun getProductNameList(): List<String>

    @Query("SELECT * FROM productData WHERE LOWER(productName) LIKE LOWER(:searchProductText) LIMIT 50 OFFSET (:page-1)*50")
    suspend fun getSearchProduct(page:Int ,searchProductText: String): List<ProductData>
}