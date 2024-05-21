package com.example.oneplusone.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.oneplusone.model.data.ProductData

@Dao
interface ProductDao {

    @Query("SELECT * FROM productData")
    suspend fun getAllProductData(): List<ProductModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductData(productData: List<ProductModel>)

    @Query("DELETE FROM productData")
    suspend fun deleteAllProductData()
}