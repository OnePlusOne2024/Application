package com.example.oneplusone.util

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.oneplusone.db.FavoriteProductDao
import com.example.oneplusone.db.FavoriteProductModel
import com.example.oneplusone.db.ProductDao
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.ProductCategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class FavoriteProductDataPagingSource(
    private val favoriteProductDao: FavoriteProductDao,
    private val mainFilterDataList: List<MainFilterData>
) : PagingSource<Int,FavoriteProductModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteProductModel> {



        // 시작 페이지
        val page = params.key ?: STARTING_PAGE

        return try {

            var data = favoriteProductDao.getAllFavoriteProductByPaging(page)



            Log.d("dbdata", data.toString())

            data=loadFilteredProductData(data)
            //테스트를 위해 고의적으로 딜레이를 줬음
            delay(500)



            // 반환할 데이터
            LoadResult.Page(
                data = data,
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            Log.d("Error", e.toString())
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.d("Error", e.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FavoriteProductModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun loadFilteredProductData(productData: List<FavoriteProductModel>): List<FavoriteProductModel> {


        return productData.filter { product ->
            mainFilterDataList.all { filter ->
                when (filter.filterType) {
                    FilterType.CONVENIENCE ->
                        filter.mainFilterText == ConvenienceType.ALL_CONVENIENCE_STORE.title || product.brand == filter.mainFilterText
                    FilterType.PRODUCT_CATEGORY ->
                        filter.mainFilterText == ProductCategoryType.ALL_PRODUCT_CATEGORY.title || product.category == filter.mainFilterText
                    FilterType.BENEFITS ->
                        filter.mainFilterText == BenefitsType.ALL_BENEFITS.title || product.benefits == filter.mainFilterText
                    FilterType.PB ->
                        if (filter.mainFilterText == "PB 상품만") {
                            product.pb
                        } else {
                            true
                        }
                    else -> false
                }
            }
        }.also { filteredProductList ->
            Log.d("FilteredProductList", filteredProductList.toString())
        }
    }

    companion object{
        private const val STARTING_PAGE = 1
    }
}