package com.example.oneplusone.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.databinding.FilterViewerBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.model.data.FilterData

class ProductFilterRecyclerAdapter(
    private val filterClickListener: FilterClickListener

): ListAdapter<FilterData, ProductFilterRecyclerAdapter.Holder>(FilterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FilterViewerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position),filterClickListener)
    }

    class Holder(private val binding: FilterViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: FilterData,filterClickListener:FilterClickListener) {
            Log.d("filter.filterImage",filter.filterText)
            binding.filterItemImage.setImageResource(filter.filterImage)
            binding.filterItemText.text=filter.filterText
//            binding.filterData=filte

            itemView.setOnClickListener {
                filterClickListener.onFilterClick(filter)
            }
        }
    }
    override fun submitList(list: List<FilterData>?) {
        super.submitList(list)
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