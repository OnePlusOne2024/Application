package com.example.oneplusone.util

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.oneplusone.db.ProductDao
import com.example.oneplusone.db.ProductData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ProductDataPagingSource(private val serverProductDao: ProductDao) : PagingSource<Int,ProductData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductData> {

        // 시작 페이지
        val page = params.key ?: STARTING_PAGE

        return try {

            val data = serverProductDao.getAllProductData(page)

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

    override fun getRefreshKey(state: PagingState<Int, ProductData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object{
        private const val STARTING_PAGE = 1
    }
}