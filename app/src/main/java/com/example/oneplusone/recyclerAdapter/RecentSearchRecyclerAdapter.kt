package com.example.oneplusone.recyclerAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.oneplusone.databinding.ProductViewerBinding
import com.example.oneplusone.databinding.RecentSearchBinding
import com.example.oneplusone.model.data.ProductData

class RecentSearchRecyclerAdapter(

): ListAdapter<String, RecentSearchRecyclerAdapter.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val productName = if (position < currentList.size) getItem(position) else ""

        holder.bind(productName)
    }
    override fun getItemCount(): Int {

        return if (currentList.size < 10) 10 else currentList.size
    }
    class Holder(private val binding: RecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productName: String) {

            binding.productName.text="${getBindingAdapterPosition()+1}. $productName"

//            itemView.setOnClickListener {
//
//            }

        }

    }
    fun submitCustomList(list: List<String>) {
        val adjustedList = if (list.size < 10) {
            list.toMutableList().apply {
                while (size < 10) add("")
            }
        } else {
            list
        }
        submitList(adjustedList)
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


