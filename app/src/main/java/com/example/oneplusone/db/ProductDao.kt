package com.example.oneplusone.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM productData")
    suspend fun getAllProductData(): List<ProductData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductData(productData: List<ProductData>)

    @Query("DELETE FROM productData")
    suspend fun deleteAllProductData()

    @Query("SELECT productName FROM productData")
    suspend fun getProductNameList(): List<String>

    @Query("SELECT * FROM productData WHERE LOWER(productName) LIKE LOWER(:searchProductText)")
    suspend fun getSearchProduct(searchProductText: String): List<ProductData>
}