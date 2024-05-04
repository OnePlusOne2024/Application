package com.example.oneplusone.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavoriteProductDao {

    @Query("SELECT * FROM favoriteProduct")
    suspend fun getAll(): List<FavoriteProductModel>

}