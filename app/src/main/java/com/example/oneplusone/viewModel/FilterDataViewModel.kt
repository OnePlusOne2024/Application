package com.example.oneplusone.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oneplusone.R
import com.example.oneplusone.model.data.FilterData


class FilterDataViewModel() : ViewModel() {

    private var _filterDataList= MutableLiveData<List<FilterData>>()
    val filterDataList: LiveData<List<FilterData>>
        get() = _filterDataList

    private fun loadItems(filterList: String) {
        val loadItems=when(filterList){
            CONVENIENCE_STORE ->listOf(
                FilterData("편의점 전체", R.drawable.all_convenience_store),
                FilterData("GS 25", R.drawable.gs_25),
                FilterData("CU", R.drawable.cu),
                FilterData("세븐 일레븐", R.drawable.seven_eleven),
                FilterData("이마트 24", R.drawable.emart_24),
            )
            PRODUCT_CATEGORY ->listOf(
                FilterData("모든 상품", R.drawable.all_product_category),
                FilterData("음료", R.drawable.drink),
                FilterData("과자", R.drawable.snack),
                FilterData("식품", R.drawable.doshirak),
                FilterData("아이스 크림", R.drawable.ice_cream),
                FilterData("생활 용품", R.drawable.soap)
            )
            BENEFITS ->listOf(
                FilterData("행사 전체", R.drawable.all_benefits),
                FilterData("", R.drawable.one_plus_one),
                FilterData("", R.drawable.two_plus_one),
                FilterData("", R.drawable.three_plus_one),
                FilterData("", R.drawable.discount),

                )
            else -> emptyList()
        }
        _filterDataList.value = loadItems
    }

    fun convenienceStoreFilterClick(){
        loadItems(CONVENIENCE_STORE)
    }

    fun productCategoryFilterClick(){
        loadItems(PRODUCT_CATEGORY)
    }

    fun productBenefitsFilterClick(){
        loadItems(BENEFITS)
    }

    companion object {
        const val CONVENIENCE_STORE: String="convenienceStore"
        const val PRODUCT_CATEGORY: String="productCategory"
        const val BENEFITS: String="benefits"
    }
}