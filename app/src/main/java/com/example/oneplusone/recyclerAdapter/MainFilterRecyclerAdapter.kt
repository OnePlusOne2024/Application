package com.example.oneplusone.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.databinding.MainFilterViewerBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enum.FilterType

class MainFilterRecyclerAdapter(
    private val mainFilterClickListener: MainFilterClickListener
): ListAdapter<MainFilterData, MainFilterRecyclerAdapter.Holder>(MainFilterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = MainFilterViewerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position),mainFilterClickListener)
    }

    class Holder(private val binding: MainFilterViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mainFilter: MainFilterData, mainFilterClickListener: MainFilterClickListener) {


            binding.mainFilterImage.setImageResource(mainFilter.mainFilterImage)
            binding.mainFilterText.text=mainFilter.mainFilterText

            itemView.setOnClickListener {
                mainFilterClickListener.onMainFilterClick(mainFilter)
            }
        }
    }

    fun updateFilterItem(filterType: FilterType, imageResource: Int, text: String) {
        val updateList = currentList.toMutableList()
        val index = updateList.indexOfFirst { it.filterType == filterType }
        if (index != -1) {
            val item = updateList[index]
            updateList[index] = item.copy(mainFilterImage = imageResource, mainFilterText = text)
            submitList(updateList) // 변경된 리스트를 제출
        }
    }
    override fun submitList(list: List<MainFilterData>?) {
        super.submitList(list)
    }

    class MainFilterDiffCallback : DiffUtil.ItemCallback<MainFilterData>() {
        override fun areItemsTheSame(oldItem: MainFilterData, newItem: MainFilterData): Boolean {
            return oldItem.mainFilterText == newItem.mainFilterText
        }
        override fun areContentsTheSame(oldItem: MainFilterData, newItem: MainFilterData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}