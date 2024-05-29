package com.example.oneplusone.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "convenienceInfo")
data class ConvenienceModel (

    @PrimaryKey(autoGenerate = true)
    val id: Long? =0,

    @ColumnInfo(name="convName")
    var convName: String,

    @ColumnInfo(name="convBrandName")
    var convBrandName: String,

    @ColumnInfo(name="latitude")
    var latitude: Double,

    @ColumnInfo(name="longitude")
    var longitude: Double,

    @ColumnInfo(name="convAddr")
    var convAddr: String,

    )