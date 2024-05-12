package com.example.oneplusone.recyclerAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ProductRankingBinding

import com.example.oneplusone.databinding.ProductViewerBinding
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.`interface`.ProductFavoriteClickListener
import com.example.oneplusone.model.data.ProductData

class ProductRankingRecyclerAdapter(

): ListAdapter<String, ProductRankingRecyclerAdapter.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ProductRankingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //10개 미만이면 공백으로 채움
        val productName = if (position < currentList.size) getItem(position) else ""

        holder.bind(productName)
    }
    override fun getItemCount(): Int {
        //강제로 10개 채우려고 작성
        return if (currentList.size < 10) 10 else currentList.size
    }
    class Holder(private val binding: ProductRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productName: String) {

            binding.productName.text="${getBindingAdapterPosition()+1}. $productName"

        }

    }

    class ProductDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}


