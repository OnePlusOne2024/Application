package com.example.oneplusone.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteProductDao {

    @Query("SELECT * FROM favoriteProduct")
    suspend fun getAll(): List<FavoriteProductModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(productData: FavoriteProductModel): Long

    @Delete
    suspend fun deleteFavoriteProduct(productData: FavoriteProductModel)
}