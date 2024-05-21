package com.example.oneplusone.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productData")
data class ProductModel (

    @PrimaryKey(autoGenerate = true)
    val id: Long? =0,

    @ColumnInfo(name="productName")
    var productName: String,

    @ColumnInfo(name="productPrice")
    var productPrice: Int,

    @ColumnInfo(name="brand")
    var brand: String,

    @ColumnInfo(name="benefits")
    var benefits: String,

    @ColumnInfo(name="productImage")
    var productImage : String,

    @ColumnInfo(name="favorite")
    var favorite: Boolean,

    @ColumnInfo(name="category")
    var category: String,

    @ColumnInfo(name="pb")
    var pb: Boolean,

)