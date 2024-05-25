package com.example.oneplusone.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ProgressbarBinding

class LoadingBarAdapter : LoadStateAdapter<LoadingBarAdapter.LoadingBarAdapterViewHolder>() {

    /**
     * LoadState 값을 받아 로딩 상태에 따라 ProgressBar의 visible 설정 처리
     */
    inner class LoadingBarAdapterViewHolder(
        private val binding: ProgressbarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.pbLoading.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadingBarAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingBarAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.loading, parent, false)
        val binding = ProgressbarBinding.bind(view)
        return LoadingBarAdapterViewHolder(binding)
    }
}