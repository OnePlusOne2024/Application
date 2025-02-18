package com.example.oneplusone.recyclerAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.databinding.ProductRankingBinding

import com.example.oneplusone.`interface`.RankingProductTextClickListener

class ProductRankingRecyclerAdapter(
    private val rankingProductTextClickListener: RankingProductTextClickListener,
): ListAdapter<String, ProductRankingRecyclerAdapter.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ProductRankingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //10개 미만이면 공백으로 채움
        val productName = getItem(position)

        holder.bind(productName,rankingProductTextClickListener)
    }

    class Holder(private val binding: ProductRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productName: String,rankingProductTextClickListener: RankingProductTextClickListener) {

            binding.productName.text="${getBindingAdapterPosition()+1}. $productName"

            itemView.setOnClickListener {
                rankingProductTextClickListener.onRankingProductTextClick(productName)
            }
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


