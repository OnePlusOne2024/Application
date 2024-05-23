package com.example.oneplusone.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.oneplusone.databinding.ActivitySearchBinding
import com.example.oneplusone.`interface`.DeleteRecentSearchClickListener
import com.example.oneplusone.`interface`.RankingProductTextClickListener
import com.example.oneplusone.`interface`.RecentSearchTextClickListener
import com.example.oneplusone.recyclerAdapter.ProductRankingRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.RecentSearchRecyclerAdapter
import com.example.oneplusone.viewModel.DataBaseViewModel
import com.example.oneplusone.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val searchViewModel:SearchViewModel by viewModels()
    private val dbViewModel: DataBaseViewModel by viewModels()

    private lateinit var productRankingAdapter: ProductRankingRecyclerAdapter
    private lateinit var recentSearchRecyclerAdapter: RecentSearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initAdapter()
        setupDataBinding()
        observeSetting()
        observeDBData()

        dbViewModel.loadProductNameList()

        binding.searchIcon.setOnClickListener {
            searchViewModel.setSearchText(binding.searchBar.text.toString())
        }

        binding.allResetRecentSearch.setOnClickListener{
            searchViewModel.deleteAllRecentSearchText(this@SearchActivity)
        }

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
        observeSearchText()
        observeSearchTextCheckResult()
    }

    private fun initProductRankingAdapter(){
        productRankingAdapter=ProductRankingRecyclerAdapter(object : RankingProductTextClickListener {
            override fun onRankingProductTextClick(rankingText: String) {
                searchViewModel.setSearchText( rankingText)
            }
        })
        binding.productRanking.adapter=productRankingAdapter
        searchViewModel.loadProductRankingList()
    }

    private fun initRecentSearchAdapter(){
        recentSearchRecyclerAdapter= RecentSearchRecyclerAdapter(
            object : RecentSearchTextClickListener {
            override fun onRecentSearchTextClick(recentSearchText: String) {
                searchViewModel.setSearchText( recentSearchText)
            }
        },object : DeleteRecentSearchClickListener{
            override fun onDeleteRecentSearchClick(recentSearchText: String) {
                searchViewModel.deleteRecentSearchText(this@SearchActivity,recentSearchText)

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
    @SuppressLint("SetTextI18n")
    private fun observeRecentSearchList(){
        searchViewModel.recentSearchList.observe(this) { recentSearchList ->
            recentSearchRecyclerAdapter.submitList(recentSearchList)

            //최근검색어의 수를 표시
            binding.recentSearchCount.text= "${recentSearchList?.size}/20"

        }
    }
    private fun observeSearchText(){
        searchViewModel.searchText.observe(this) { searchText ->
            Log.d("searchText2", searchText.toString())
            moveSearchResultActivity(searchText)
        }
    }
    private fun observeSearchTextCheckResult(){
        searchViewModel.searchTextCheckResult.observe(this) { searchTextCheckResult ->
            Log.d("searchTextResult", searchTextCheckResult.toString())
            if(!searchTextCheckResult){
                Toast.makeText(this@SearchActivity, "올바른 검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun observeDBData(){
        dbViewModel.productNameList.observe(this) { productNameList ->

            if(productNameList!=null){
                searchViewModel.updateProductNameList(productNameList)
            }
        }
    }

    //최근검색어,인기검색어,직접입력한 검색어,연관상품을 검색했을때 다른 엑티비티로 데이터를 전송하는 기능을함
    private fun moveSearchResultActivity(searchText: String? = null) {
        searchText?.let{

            val searchActivityIntent = Intent(this, SearchResultActivity::class.java).apply {
                putExtra("searchText", searchText)
            }
            startActivity(searchActivityIntent)
            searchViewModel.saveSearchText(context = this, searchText)
        }
    }
}