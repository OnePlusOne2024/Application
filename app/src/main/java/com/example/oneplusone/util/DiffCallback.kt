package com.example.oneplusone.util

import androidx.recyclerview.widget.DiffUtil
import com.example.oneplusone.model.ProductData
import com.example.oneplusone.model.ProductDataViewModel

class DiffCallback : DiffUtil.ItemCallback<ProductData>() {
    override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
        return oldItem == newItem
    }
}