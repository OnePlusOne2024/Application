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

class ProductItemRecyclerAdapter(
    private val productClickListener: ProductClickListener,
    private val productFavoriteClickListener: ProductFavoriteClickListener
): ListAdapter<ProductData, ProductItemRecyclerAdapter.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ProductViewerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position),productClickListener,productFavoriteClickListener)
    }

    class Holder(private val binding: ProductViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: ProductData,
            productClickListener: ProductClickListener,
            productFavoriteClickListener: ProductFavoriteClickListener
        ) {

            binding.productData=product

            itemView.setOnClickListener {
                productClickListener.onItemClick(product)
            }


            binding.favorite.setOnClickListener {
                productFavoriteClickListener.onFavoriteClick(product)
                updateFavoriteButtonImage(binding.favorite, product.favorite)

            }
        }
        //일단 여기서 이미지를 바꾸기로
    private fun updateFavoriteButtonImage(button: ImageButton, isFavorite: Boolean) {
        button.setImageResource(if (isFavorite) R.drawable.favorite_on else R.drawable.favorite_off)
        Log.d("isFavorite", isFavorite.toString())
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


