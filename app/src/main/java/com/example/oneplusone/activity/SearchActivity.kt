package com.example.oneplusone.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ActivitySearchBinding
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.RankingProductTextClickListener
import com.example.oneplusone.`interface`.RecentSearchTextClickListener
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.recyclerAdapter.ProductRankingRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.RecentSearchRecyclerAdapter
import com.example.oneplusone.util.FilterStyle
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


        binding.searchIcon.setOnClickListener {
            moveSearchResultActivity()
        }
//        moveSearchResultActivity()
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
        productRankingAdapter=ProductRankingRecyclerAdapter(object : RankingProductTextClickListener {
            override fun onRankingProductTextClick(rankingText: String) {
                moveSearchResultActivity(rankingText)
            }
        })
        binding.productRanking.adapter=productRankingAdapter
        searchViewModel.loadProductRankingList()
    }

    private fun initRecentSearchAdapter(){
        recentSearchRecyclerAdapter= RecentSearchRecyclerAdapter(object : RecentSearchTextClickListener {
            override fun onRecentSearchTextClick(recentSearchText: String) {
                moveSearchResultActivity(recentSearchText)
            }
        })
        binding.recentSearches.adapter=recentSearchRecyclerAdapter
        searchViewModel.loadRecentSearchList(context = this)
    }

    private fun observeProductNameList(){
        searchViewModel.productNameList.observe(this) { productNames ->

            //AutoCompleteTextView를 사용해 연관 검색어를 보여줌
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productNames)

            binding.searchBar.setAdapter(adapter)
            binding.searchBar.threshold = 1


            //연관검색어 터치시 바로 검색
            binding.searchBar.setOnItemClickListener { parent, _, position, _ ->
                val selectedSearchText = parent.getItemAtPosition(position) as String
                moveSearchResultActivity(selectedSearchText)
            }
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

    //최근검색어,인기검색어,직접입력한 검색어,연관상품을 검색했을때 다른 엑티비티로 데이터를 전송하는 기능을함
    private fun moveSearchResultActivity(searchText: String? = null) {
        val finalSearchText = searchText ?: binding.searchBar.text.toString()

        val searchActivityIntent = Intent(this, SearchResultActivity::class.java).apply {
            putExtra("searchText", finalSearchText)
        }
        startActivity(searchActivityIntent)
        searchViewModel.saveSearchText(context = this, finalSearchText)
    }
}