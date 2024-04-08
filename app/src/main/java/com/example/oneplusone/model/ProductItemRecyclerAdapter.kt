package com.example.oneplusone.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.oneplusone.databinding.ProductViewerBinding
import com.example.oneplusone.util.DiffCallback

class ProductItemRecyclerAdapter(
    private val productClickListener: ProductClickListener
): ListAdapter<ProductData, ProductItemRecyclerAdapter.Holder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ProductViewerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), productClickListener)
    }

    class Holder(private val binding: ProductViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductData, productClickListener: ProductClickListener) {

            binding.productName.text=product.productName

            with(binding) {
                itemView.setOnClickListener {
                    Log.d("클릭","클릭")
                }
            }
        }
    }

    class ProductClickListener(val clickListener: (product: ProductData) -> Unit) {
        fun onClick(product: ProductData) = clickListener(product)
    }
}


