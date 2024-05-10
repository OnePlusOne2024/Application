package com.example.oneplusone.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.R

import com.example.oneplusone.databinding.ProductViewerBinding
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.`interface`.ProductFavoriteClickListener
import com.example.oneplusone.model.data.ProductData

class ProductRankingRecyclerAdapter(


): ListAdapter<ProductData, ProductRankingRecyclerAdapter.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ProductViewerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(private val binding: ProductViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: ProductData,

        ) {

            binding.productData=product

            itemView.setOnClickListener {

            }

        }

    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductData>() {
        override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem == newItem
        }
    }
}


