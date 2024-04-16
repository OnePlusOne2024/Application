package com.example.oneplusone.model.data.enum

import com.example.oneplusone.R

enum class ProductCategoryType(val iconResId: Int, val title: String) {

    ALL_PRODUCT_CATEGORY(R.drawable.all_product_category, "모든 상품"),
    PRODUCT_CATEGORY_DRINK(R.drawable.drink, "음료"),
    PRODUCT_CATEGORY_SNACK(R.drawable.snack, "과자"),
    PRODUCT_CATEGORY_FOOD(R.drawable.doshirak, "식품"),
    PRODUCT_CATEGORY_ICE_CREAM(R.drawable.ice_cream, "아이스 크림"),
    PRODUCT_CATEGORY_HOUSEHOLD_SUPPLIES(R.drawable.soap, "생활 용품"),

}