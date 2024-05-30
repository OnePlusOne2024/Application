package com.example.oneplusone.model.data

import com.google.android.gms.maps.model.LatLng


data class ConvenienceData(

    val convenienceType: String,

    val convenienceName: String,

    val convenienceAddress:String,

    val conveniencePosition: LatLng,
    )
