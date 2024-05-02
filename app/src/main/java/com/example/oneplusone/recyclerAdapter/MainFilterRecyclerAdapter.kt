package com.example.oneplusone.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.databinding.MainFilterViewerBinding
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enums.FilterType

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

                mainFilterClickListener.onMainFilterClick(mainFilter,itemView)
            }
        }
    }

//    fun updateFilterItem(filterData: FilterData): List<MainFilterData> {
//
//        val updatedList = currentList.map { currentItem ->
//            if (currentItem.filterType == filterData.filterType) {
//                currentItem.copy(mainFilterImage = filterData.filterImage, mainFilterText = filterData.filterText)
//            } else currentItem
//        }
//        Log.d("updatedList", updatedList.toString())
//        submitList(updatedList)
//        return updatedList
//    }


    class MainFilterDiffCallback : DiffUtil.ItemCallback<MainFilterData>() {
        override fun areItemsTheSame(oldItem: MainFilterData, newItem: MainFilterData): Boolean {
            return oldItem.mainFilterText == newItem.mainFilterText
        }
        override fun areContentsTheSame(oldItem: MainFilterData, newItem: MainFilterData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}