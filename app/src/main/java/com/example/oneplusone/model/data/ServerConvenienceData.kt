package com.example.oneplusone.model.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class ServerConvenienceData(

    val id: Long? =0,

    var convName: String,

    var convBrandName: String,

    var latitude: Double,

    var longitude: Double,

    var convAddr: String,
)
