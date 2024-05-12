package com.example.oneplusone.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.repository.ProductDataRepository
import com.example.oneplusone.repository.ProductRankingDataRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRankingDataRepository: ProductRankingDataRepository
): ViewModel() {

    private val _productNameList=MutableLiveData<ArrayList<String>>()
    private val _productRanking=MutableLiveData<List<String>>()
    private val _recentSearchList=MutableLiveData<MutableList<String>>()
    private val _inputText=MutableLiveData<String>()

    val productNameList: LiveData<ArrayList<String>>
        get() = _productNameList

    val productRanking: LiveData<List<String>>
        get() = _productRanking

    val recentSearchList: LiveData<MutableList<String>>
        get() = _recentSearchList

    val inputText: LiveData<String>
        get() = _inputText


    fun updateProductNameList(productNames: ArrayList<String>) {
        _productNameList.value = productNames
    }


    fun loadProductRankingList(){
        _productRanking.value=productRankingDataRepository.loadProductRankingData()
    }


    fun loadRecentSearchList(context: Context){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val savedString = sharedPreferences.getString("recentSearchText", null)
        val recentSearchList = savedString?.split(",")?.toMutableList() ?: mutableListOf()

        _recentSearchList.value=recentSearchList
        Log.d("recentSearchList.value",_recentSearchList.value.toString())
    }

    @SuppressLint("CommitPrefEdits")
    fun saveSearchText(context: Context, searchText: String){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val recentSearchList = _recentSearchList.value ?: mutableListOf()

        if (!recentSearchList.contains(searchText)) {
            recentSearchList.add(0, searchText)

            while (recentSearchList.size > 10) {
                recentSearchList.removeAt(recentSearchList.size - 1)
            }
            _recentSearchList.value = recentSearchList

            val savedString = recentSearchList.joinToString(",")
            editor.putString("recentSearchText", savedString)
            editor.apply()
        }
    }

}