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
    suspend fun getAllFavoriteProduct(): List<FavoriteProductModel>

    @Query("SELECT * FROM favoriteProduct ORDER BY id ASC LIMIT 50 OFFSET (:page-1)*50")
    suspend fun getAllFavoriteProductByPaging(page:Int): List<FavoriteProductModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(productData: FavoriteProductModel): Long

    @Delete
    suspend fun deleteFavoriteProduct(productData: FavoriteProductModel)

    @Query("DELETE FROM favoriteProduct")
    suspend fun deleteAllFavoriteProduct()


    @Query("SELECT * FROM favoriteProduct WHERE LOWER(productName) LIKE LOWER(:searchProductText)")
    suspend fun getSearchProduct(searchProductText: String): List<FavoriteProductModel>
}