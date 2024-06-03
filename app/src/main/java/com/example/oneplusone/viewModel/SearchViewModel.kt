package com.example.oneplusone.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.model.data.ProductRanking
import com.example.oneplusone.repository.ProductRankingDataRepository
import com.example.oneplusone.util.SearchTextCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRankingDataRepository: ProductRankingDataRepository
): ViewModel() {

    private val _productNameList=MutableLiveData<List<String>>()
    private val _productRanking=MutableLiveData<List<String>?>()
    private val _recentSearchList=MutableLiveData<MutableList<String>?>()
    private val _inputText=MutableLiveData<String>()
    private val _searchText=MutableLiveData<String>()
    private val _searchTextCheckResult=MutableLiveData<Boolean>()




    val productNameList: LiveData<List<String>>
        get() = _productNameList

    val productRanking: LiveData<List<String>?>
        get() = _productRanking

    val recentSearchList: LiveData<MutableList<String>?>
        get() = _recentSearchList

    val inputText: LiveData<String>
        get() = _inputText

    val searchText: LiveData<String>
        get() = _searchText
    val searchTextCheckResult: LiveData<Boolean>
        get() = _searchTextCheckResult

    fun updateProductNameList(productNamesList: List<String>) {
        _productNameList.value = productNamesList
    }

    fun loadProductRankingList() {
        productRankingDataRepository.loadProductRankingData { productSearchRanking ->
            Log.d("serverProductData", productSearchRanking.toString())
            val productRankingList = productSearchRanking?.let {
                convertProductRankingListToString(it)
            } ?: emptyList()
            _productRanking.value = paddingProductSearchRanking(productRankingList)
        }
    }

    private fun convertProductRankingListToString(productSearchRanking: List<ProductRanking>): List<String> =
        productSearchRanking.map { singleProductName ->
            Log.d("singleProductName", singleProductName.productName)
            singleProductName.productName
        }

    private fun paddingProductSearchRanking(productSearchRanking: List<String>): List<String> =
        productSearchRanking.toMutableList().apply {
            while (size < 10) add("")
        }


    fun loadRecentSearchList(context: Context){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val savedString = sharedPreferences.getString("recentSearchText", null)

        val recentSearchList = if (!savedString.isNullOrEmpty()) {
            savedString.split(",").toMutableList()
        } else {
            mutableListOf()
        }

        _recentSearchList.value=recentSearchList
    }

    //todo 최근 검색어 삭제하는 로직 구현하기
    fun saveSearchText(context: Context, searchText: String){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val savedString = sharedPreferences.getString("recentSearchText", null)
        //빈문자열을 split할때 이것까지 아이템에 포함되어 나왔기 때문에 수정
        val recentSearchList = if (!savedString.isNullOrEmpty()) {
            savedString.split(",").toMutableList()
        } else {
            mutableListOf()
        }

        if (!recentSearchList.contains(searchText)) {
            recentSearchList.add(0, searchText)

            while (recentSearchList.size > 20) {
                recentSearchList.removeAt(recentSearchList.size - 1)
            }

            val newSavedString = recentSearchList.joinToString(",")
            editor.putString("recentSearchText", newSavedString)
            editor.apply()
            _recentSearchList.value=recentSearchList
        }
    }


    fun deleteRecentSearchText(context: Context, searchText: String){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        val savedString = sharedPreferences.getString("recentSearchText", "")

        val recentSearchList = savedString?.split(",")?.toMutableList() ?: mutableListOf()

        if (recentSearchList.contains(searchText)) {
            recentSearchList.remove(searchText)

            val newSavedString = recentSearchList.joinToString(",")
            editor.putString("recentSearchText", newSavedString)
            editor.apply()
            _recentSearchList.value=recentSearchList

        }
    }


    fun deleteAllRecentSearchText(context: Context){
        val sharedPreferences = context.getSharedPreferences("RecentSearchText", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("recentSearchText", null)
        editor.apply()
        _recentSearchList.value=null
    }

    fun setSearchText(searchText: String){
        _searchTextCheckResult.value= SearchTextCheck().searchTextCheck(searchText)
        if(_searchTextCheckResult.value==true){

            this._searchText.value=searchText
        }
    }

    fun postCurrentSearchText(searchText: String){
        productRankingDataRepository.postCurrentSearchText(searchText)
    }
}