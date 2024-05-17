package com.example.oneplusone.recyclerAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.oneplusone.databinding.ProductViewerBinding
import com.example.oneplusone.databinding.RecentSearchBinding
import com.example.oneplusone.`interface`.DeleteRecentSearchClickListener
import com.example.oneplusone.`interface`.RankingProductTextClickListener
import com.example.oneplusone.`interface`.RecentSearchTextClickListener
import com.example.oneplusone.model.data.ProductData

class RecentSearchRecyclerAdapter(
    private val recentSearchTextClickListener: RecentSearchTextClickListener,
    private val deleteRecentSearchClickListener: DeleteRecentSearchClickListener,

): ListAdapter<String, RecentSearchRecyclerAdapter.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(getItem(position),recentSearchTextClickListener,deleteRecentSearchClickListener)
    }
//    override fun getItemCount(): Int {
//
//
//        return if (currentList.size < 10) 10 else currentList.size
//    }
    class Holder(private val binding: RecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            productName: String,
            recentSearchTextClickListener: RecentSearchTextClickListener,
            deleteRecentSearchClickListener:DeleteRecentSearchClickListener
        ) {

            binding.productName.text=productName


            itemView.setOnClickListener {
                recentSearchTextClickListener.onRecentSearchTextClick(productName)
            }
            binding.deleteRecentSearchText.setOnClickListener{
                deleteRecentSearchClickListener.onDeleteRecentSearchClick(productName)

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


