package com.example.oneplusone.model.data.enums

import com.example.oneplusone.R

enum class BenefitsType (val iconResId: Int, val title: String) {

    ALL_BENEFITS(R.drawable.all_benefits, "행사 전체"),
    BENEFITS_ONE_PLUS_ONE(R.drawable.one_plus_one, "1+1"),
    BENEFITS_TWO_PLUS_ONE(R.drawable.two_plus_one, "2+1"),
    BENEFITS_THREE_PLUS_ONE(R.drawable.three_plus_one, "3+1"),
    BENEFITS_DISCOUNT(R.drawable.discount, "할인"),
}