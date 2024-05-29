package com.example.oneplusone.db

import androidx.room.Dao
import androidx.room.Query


@Dao
interface ConvenienceDao {

    @Query("SELECT * FROM convenienceInfo")
    suspend fun getAllFavoriteProduct(): List<ConvenienceModel>
}