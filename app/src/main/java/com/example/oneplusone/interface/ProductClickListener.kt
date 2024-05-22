package com.example.oneplusone.`interface`

import com.example.oneplusone.db.ProductData

interface ProductClickListener {
    fun onItemClick(productData: ProductData)
}