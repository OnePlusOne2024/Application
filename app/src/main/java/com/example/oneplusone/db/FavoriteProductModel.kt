package com.example.oneplusone.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteProduct")
data class FavoriteProductModel (

    @PrimaryKey(autoGenerate = true)
    val id: Int? =0,

    @ColumnInfo(name="productName")
    var productName: String,

    @ColumnInfo(name="productPrice")
    var productPrice: Int,

    @ColumnInfo(name="brand")
    var brand: String,

    @ColumnInfo(name="benefits")
    var benefits: String,

    @ColumnInfo(name="productImage")
    var productImage : Int,

    @ColumnInfo(name="favorite")
    var favorite: Boolean,

    @ColumnInfo(name="category")
    var category: String,

    @ColumnInfo(name="pb")
    var pb: Boolean,

)