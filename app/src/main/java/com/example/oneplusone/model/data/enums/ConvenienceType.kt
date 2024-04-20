package com.example.oneplusone.model.data.enums

import com.example.oneplusone.R

enum class ConvenienceType (val iconResId: Int, val title: String) {

    ALL_CONVENIENCE_STORE(R.drawable.all_convenience_store, "편의점 전체"),
    STORE_GS_25(R.drawable.gs25_logo2, "GS 25"),
    STORE_CU(R.drawable.cu_logo2, "CU"),
    STORE_SEVEN_ELEVEN(R.drawable.seven_logo2, "세븐 일레븐"),
    STORE_E_MART24(R.drawable.emar_24_log_2, "이마트 24")

}