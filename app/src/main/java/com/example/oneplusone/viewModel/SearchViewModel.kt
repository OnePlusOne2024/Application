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
import com.example.oneplusone.util.SearchTextCheck
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
    private val _searchText=MutableLiveData<String>()
    private val _searchTextCheckResult=MutableLiveData<Boolean>()

    val productNameList: LiveData<ArrayList<String>>
        get() = _productNameList

    val productRanking: LiveData<List<String>>
        get() = _productRanking

    val recentSearchList: LiveData<MutableList<String>>
        get() = _recentSearchList

    val inputText: LiveData<String>
        get() = _inputText

    val searchText: LiveData<String>
        get() = _searchText
    val searchTextCheckResult: LiveData<Boolean>
        get() = _searchTextCheckResult

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

    //todo 최근 검색어 삭제하는 로직 구현하기
    @SuppressLint("CommitPrefEdits")
    fun saveSearchText(context: Context, searchText: String){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val savedString = sharedPreferences.getString("recentSearchText", "")
        val recentSearchList = savedString?.split(",")?.toMutableList() ?: mutableListOf()

        if (!recentSearchList.contains(searchText)) {
            recentSearchList.add(0, searchText)

            while (recentSearchList.size > 10) {
                recentSearchList.removeAt(recentSearchList.size - 1)
            }

            val newSavedString = recentSearchList.joinToString(",")
            editor.putString("recentSearchText", newSavedString)
            editor.apply()
        }
    }

    fun setSearchText(searchText: String){
        _searchTextCheckResult.value= SearchTextCheck().searchTextCheck(searchText)
        if(_searchTextCheckResult.value==true){
            this._searchText.value=searchText
        }
    }
}