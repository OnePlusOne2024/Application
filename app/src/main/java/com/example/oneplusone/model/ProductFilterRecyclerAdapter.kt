package com.example.oneplusone.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.databinding.FilterViewerBinding
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.ProductData

class ProductFilterRecyclerAdapter (
//    private val filterClickListener: FilterClickListener
): ListAdapter<FilterData, ProductFilterRecyclerAdapter.Holder>(FilterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FilterViewerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(private val binding: FilterViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: FilterData) {
            with(binding) {

                binding.filterData=filter

//                itemView.setOnClickListener {
//                    Log.d("클릭","클릭")
//                }
            }
        }
    }
    override fun submitList(list: List<FilterData>?) {
        super.submitList(list)
    }
    class FilterClickListener(val clickListener: (product: ProductData) -> Unit) {
        fun onClick(product: ProductData) = clickListener(product)
    }


    class FilterDiffCallback : DiffUtil.ItemCallback<FilterData>() {
        override fun areItemsTheSame(oldItem: FilterData, newItem: FilterData): Boolean {
            return oldItem.filterText == newItem.filterText
        }
        override fun areContentsTheSame(oldItem: FilterData, newItem: FilterData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}