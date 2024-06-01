package com.example.oneplusone.util

import android.util.Log
import android.view.View
import androidx.paging.LoadState
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter

class ProductItemRecyclerAdapterStateManagement(
    private val adapter: ProductItemRecyclerAdapter,
    private val loadingImage: View,
    private val emptyImage: View
) {
    init {
        setupLoadStateListener()
    }


    private fun setupLoadStateListener() {
        adapter.addLoadStateListener { loadState ->
            val isLoading = loadState.source.refresh is LoadState.Loading ||
                    loadState.source.prepend is LoadState.Loading ||
                    loadState.source.append is LoadState.Loading

            val isEmpty = adapter.itemCount < 1

            if (isLoading) {
                Log.d("로딩", "로드중")
                loadingImage.visibility = View.VISIBLE
                emptyImage.visibility = View.GONE
            } else if (isEmpty) {
                Log.d("로딩", "빔")
                emptyImage.visibility = View.VISIBLE
                loadingImage.visibility = View.GONE
            } else {
                Log.d("로딩", "있음")
                emptyImage.visibility = View.GONE
                loadingImage.visibility = View.GONE
            }
        }
    }
}