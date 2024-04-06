package com.example.oneplusone.model

data class ProductData(

    var productId:Int,

    var productName: String,

    var productPrice: Int,

    var brand: String,

    var benefits: String,

    var productImage : ByteArray,

    var favorite: Boolean,

)
