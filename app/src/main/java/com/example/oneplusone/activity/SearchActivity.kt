package com.example.oneplusone.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ActivitySearchBinding
import com.example.oneplusone.recyclerAdapter.ProductRankingRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.RecentSearchRecyclerAdapter
import com.example.oneplusone.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val searchViewModel:SearchViewModel by viewModels()


    private lateinit var productRankingAdapter: ProductRankingRecyclerAdapter
    private lateinit var recentSearchRecyclerAdapter: RecentSearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val productNameList=intent.getStringArrayListExtra("productNameList")

        if (productNameList != null) {
            searchViewModel.updateProductNameList(productNameList)
        }

        initAdapter()
        setupDataBinding()
        observeSetting()


//        addTextChangedListener()
    }


    private fun setupDataBinding() {
        binding.apply {
            searchViewModel = this@SearchActivity.searchViewModel

            lifecycleOwner = this@SearchActivity
        }
    }
    private fun initAdapter() {
        initProductRankingAdapter()
        initRecentSearchAdapter()
    }

    private fun observeSetting(){
        observeProductNameList()
        observeProductRanking()
        observeRecentSearchList()
    }

    private fun initProductRankingAdapter(){
        productRankingAdapter=ProductRankingRecyclerAdapter()
        binding.productRanking.adapter=productRankingAdapter
        searchViewModel.loadProductRankingList()
    }

    private fun initRecentSearchAdapter(){
        recentSearchRecyclerAdapter= RecentSearchRecyclerAdapter()
        binding.recentSearches.adapter=recentSearchRecyclerAdapter
        searchViewModel.loadRecentSearchList(context = this)
    }

    private fun observeProductNameList(){
        searchViewModel.productNameList.observe(this) { productNames ->

            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productNames)

            binding.searchBar.setAdapter(adapter)
            binding.searchBar.threshold = 1

        }
    }
    private fun observeProductRanking(){
        searchViewModel.productRanking.observe(this) { productRanking ->
            productRankingAdapter.submitList(productRanking)
        }
    }
    private fun observeRecentSearchList(){
        searchViewModel.recentSearchList.observe(this) { recentSearchList ->
            recentSearchRecyclerAdapter.submitList(recentSearchList)
        }
    }

    private fun moveSearchResultActivity(searchText:String){
        val searchActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        }

        binding.searchIcon.setOnClickListener {
            val searchActivityIntent = Intent(this, SearchResultActivity::class.java)
            searchActivityIntent.putExtra("searchText",searchText)
            searchActivityResult.launch(searchActivityIntent)
            searchViewModel.saveSearchText(context = this,searchText)
        }
    }
}