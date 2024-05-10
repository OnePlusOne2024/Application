package com.example.oneplusone.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ActivitySearchBinding
import com.example.oneplusone.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val searchViewModel:SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val productNameList=intent.getStringArrayListExtra("productNameList")

        if (productNameList != null) {
            searchViewModel.updateProductNameList(productNameList)
        }

        observeSetting()
    }


    private fun setupDataBinding() {
        binding.apply {
//            searchViewModel = this@SearchActivity.mainFilterViewModel
//
//            lifecycleOwner = viewLifecycleOwner
        }
    }


    private fun observeSetting(){
        observeProductNameList()
    }

    private fun observeProductNameList(){
        searchViewModel.productNameList.observe(this) { productNames ->

            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productNames)

            binding.searchBar.setAdapter(adapter)
            binding.searchBar.threshold = 1
        }
    }
}