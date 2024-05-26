package com.example.oneplusone.model.data

//서버에서 데이터를 보낼때 result만 받기 위해서 데이터클래스를 하나 더 만들었음
data class ServerResponse(
    val success:Boolean,
    val result: List<ServerProductData>,

)