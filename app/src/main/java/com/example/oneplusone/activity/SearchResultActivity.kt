package com.example.oneplusone.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.paging.map
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ActivitySearchResultBinding
import com.example.oneplusone.databinding.ProductDetailViewerBinding
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.`interface`.ProductFavoriteClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.util.FilterAnimated
import com.example.oneplusone.util.FilterStyle
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.DataBaseViewModel
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.MainFilterViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import com.example.oneplusone.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//todo 잘못된 검색어(빈칸,특수문자?,글자수?)제어 팝업 만들어야함
@AndroidEntryPoint
class SearchResultActivity : AppCompatActivity() {

    private val binding by lazy {ActivitySearchResultBinding.inflate(layoutInflater) }

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val filterDataViewModel: FilterDataViewModel by viewModels()
    private val mainFilterViewModel: MainFilterViewModel by viewModels()
    private val dbViewModel: DataBaseViewModel by viewModels()
    private val searchViewModel:SearchViewModel by viewModels()

    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
    private lateinit var mainFilterAdapter: MainFilterRecyclerAdapter

    private var oldSearchText:String?=null

    private val productSpacingController = ItemSpacingController(25, 25, 40)

    private var selectMainFilter: View?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()

        setupDataBinding()

        observeSetting()
        dbViewModel.loadFavoriteProducts()
        dbViewModel.loadProductNameList()

        oldSearchText=intent.getStringExtra("searchText")
        oldSearchText?.let { searchViewModel.setSearchText(it) }

        binding.searchBar.setText(oldSearchText)


        binding.searchIcon.setOnClickListener {
            searchViewModel.setSearchText( binding.searchBar.text.toString())
        }
    }

    private fun initAdapter() {
        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
    }

    private fun setupDataBinding() {
        binding.apply {
            mainFilterViewModel = this@SearchResultActivity.mainFilterViewModel
            filterDataViewModel = this@SearchResultActivity.filterDataViewModel
            productDataViewModel = this@SearchResultActivity.productDataViewModel
            searchViewModel = this@SearchResultActivity.searchViewModel
            lifecycleOwner = this@SearchResultActivity
        }
    }

    private fun observeSetting(){
        observeMainFilterViewModel()
        observeFilterDataViewModel()
        observeProductDataViewModel()
        observeDataBaseViewModel()
        observeSearchViewModel()
    }

    private fun initMainFilterAdapter() {
        mainFilterAdapter = MainFilterRecyclerAdapter(object : MainFilterClickListener {
            override fun onMainFilterClick(mainFilter: MainFilterData, itemView: View) {

                FilterStyle().resetPreviousFilterStyle(this@SearchResultActivity,selectMainFilter)

                selectMainFilter = itemView
                filterDataViewModel.showFilter(mainFilter.filterType)

            }
        })
        binding.mainFilterViewer.adapter = mainFilterAdapter

    }

    private fun initProductFilterAdapter() {
        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {

            override fun onFilterClick(filterData: FilterData) {

                mainFilterViewModel.updateMainFilter(filterData)

                //세부 필터를 고르면 불러온 데이터를 제거함
                filterDataViewModel.clearFilterData()

            }

        })
        binding.filterViewer.adapter = productFilterAdapter
    }

    private fun initProductItemRecyclerAdapter() {
        productItemRecyclerAdapter = ProductItemRecyclerAdapter(
            object : ProductClickListener {
                override fun onItemClick(productData: ProductData) {
                    productDataViewModel.loadClickProductData(productData)
                }
            }, object : ProductFavoriteClickListener {
                override fun onFavoriteClick(productData: ProductData) {
                    productDataViewModel.toggleFavorite(productData)
                }
            })
        binding.productGridView.adapter = productItemRecyclerAdapter
        binding.productGridView.addItemDecoration(productSpacingController)
    }

    private fun observeMainFilterViewModel() {
        mainFilterViewModel.mainFilterDataList.observe(this) { mainFilterData ->

            mainFilterAdapter.submitList(mainFilterData)
            productDataViewModel.setCurrentMainFilterData(mainFilterData)
            dbViewModel.loadSearchProductDataByPaging()

        }

    }

    private fun observeFilterDataViewModel() {
        //여기서 변화를 감지하고 변경함
        filterDataViewModel.filterDataList.observe(this) { data ->
            productFilterAdapter.submitList(data)

        }

        filterDataViewModel.filterBar.observe(this) { isVisible  ->
            //사라질 때 시각적으로 버벅 거림이 느껴져서 애니메이션으로 부드럽게 바꿨음
            FilterAnimated().viewAnimated(isVisible,binding.filterBarDetail)

        }

        filterDataViewModel.selectFilterColorSwitch.observe(this) { switchState ->

            if(switchState){
                FilterStyle().updateFilterStyle(this@SearchResultActivity,selectMainFilter)
            }
            else{
                FilterStyle().resetPreviousFilterStyle(this@SearchResultActivity,selectMainFilter)
            }
        }
    }

    private fun observeProductDataViewModel() {
        productDataViewModel.productDataList.observe(this) { data ->
//            productItemRecyclerAdapter.submitList(data)
        }

        productDataViewModel.clickProductData.observe(this){ clickProductData ->
            showProductDetailDialog(clickProductData)
        }
        productDataViewModel.filterProductData.observe(this){ filterProductData ->
//            productItemRecyclerAdapter.submitList(filterProductData)
        }

        productDataViewModel.isFavorite.observe(this) { isFavorite ->
            dbViewModel.favoriteProductJudgment(isFavorite)
        }


    }

    private fun observeDataBaseViewModel() {
        dbViewModel.favoriteProducts.observe(this) { favoriteProductData ->
            productDataViewModel.loadFavoriteProduct(favoriteProductData)
        }
//        dbViewModel.favoriteProducts.observe(this) { favoriteProductData ->
//            productDataViewModel.loadFavoriteProduct(favoriteProductData)
//        }
        
        dbViewModel.DBProductDataList.observe(this) { dbProductDataList ->
            lifecycleScope.launch {

                dbProductDataList.collectLatest { pagingData ->

                    val transformedData = pagingData
                        .map { productData ->
                            Log.d("productData4", productData.toString())
                            productDataViewModel.isProductFavorite(productData)
                        }.filter{
                            productDataViewModel.loadFilteredProductData(it)
                        }

                    productItemRecyclerAdapter.submitData(lifecycle, transformedData)

                }

            }
        }
        dbViewModel.productNameList.observe(this) { productNameList ->
            if(productNameList!=null){
                searchViewModel.updateProductNameList(productNameList)
            }
        }
    }

    private fun observeSearchViewModel(){
        searchViewModel.searchText.observe(this) { newSearchText ->


            val convertSearchText="%${newSearchText}%"
            dbViewModel.setSearchText(convertSearchText)


            dbViewModel.loadSearchProductDataByPaging()
//            dbViewModel.loadSearchFavoriteProducts(convertSearchText)
//            dbViewModel.loadSearchProductDataList(convertSearchText)


            //새로운 검색을 했다면 검색된 상품만 다시 불러옴
//            productDataViewModel.loadProductData(null,newSearchText)
            //검색한 데이터를 저장함
            searchViewModel.saveSearchText(this@SearchResultActivity,newSearchText)
        }
        searchViewModel.searchTextCheckResult.observe(this) { searchTextCheckResult ->
            Log.d("searchTextResult", searchTextCheckResult.toString())
            if(!searchTextCheckResult){
                Toast.makeText(this@SearchResultActivity, "올바른 검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }
        searchViewModel.productNameList.observe(this) { productNames ->

            //AutoCompleteTextView를 사용해 연관 검색어를 보여줌
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productNames)

            binding.searchBar.setAdapter(adapter)
            binding.searchBar.threshold = 1


            //연관검색어 터치시 바로 검색
            binding.searchBar.setOnItemClickListener { parent, _, position, _ ->
                val selectedSearchText = parent.getItemAtPosition(position) as String
                searchViewModel.setSearchText(selectedSearchText)
            }
        }
    }


    private fun showProductDetailDialog(productData: ProductData) {
        val mDialogView = LayoutInflater.from(this@SearchResultActivity).inflate(R.layout.product_detail_viewer, null)
        val dialogBinding = ProductDetailViewerBinding.bind(mDialogView)


        dialogBinding.productData = productData


        dialogBinding.favorite.setOnClickListener{
            productDataViewModel.toggleFavorite(productData)

            if (productData.favorite) {
                dialogBinding.favorite.setImageResource(R.drawable.favorite_on)
            } else {
                dialogBinding.favorite.setImageResource(R.drawable.favorite_off)
            }

        }
        val mBuilder = AlertDialog.Builder(this@SearchResultActivity)
            .setView(mDialogView)


        val dialog = mBuilder.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setOnDismissListener {
//            if (index != -1) {
//                productItemRecyclerAdapter.notifyItemChanged(index)
//            }
        }
    }


}