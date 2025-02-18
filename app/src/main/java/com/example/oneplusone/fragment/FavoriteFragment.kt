package com.example.oneplusone.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.paging.map
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentFavoriteBinding
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
import com.example.oneplusone.util.ProductItemRecyclerAdapterStateManagement
import com.example.oneplusone.viewModel.DataBaseViewModel
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.MainFilterViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val filterDataViewModel: FilterDataViewModel by viewModels()
    private val mainFilterViewModel: MainFilterViewModel by viewModels()
    private val dbViewModel: DataBaseViewModel by viewModels()

    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
    private lateinit var mainFilterAdapter: MainFilterRecyclerAdapter

    private val productSpacingController = ItemSpacingController(25, 25, 40)

    private var selectMainFilter:View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAdapter()

        setupDataBinding()

        observeSetting()



    }

    private fun initAdapter() {
        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
        ProductItemRecyclerAdapterStateManagement(
            adapter = productItemRecyclerAdapter,
            loadingImage = binding.progressBarImage,
            emptyImage = binding.emptyProduct
        )
    }

    private fun setupDataBinding() {
        binding.apply {
            mainFilterViewModel = this@FavoriteFragment.mainFilterViewModel
            filterDataViewModel = this@FavoriteFragment.filterDataViewModel
            productDataViewModel = this@FavoriteFragment.productDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun observeSetting(){
        observeMainFilterViewModel()
        observeFilterDataViewModel()
        observeProductDataViewModel()
        observeDataBaseViewModel()
    }

    private fun initMainFilterAdapter() {
        mainFilterAdapter = MainFilterRecyclerAdapter(object : MainFilterClickListener {
            override fun onMainFilterClick(mainFilter: MainFilterData, itemView: View) {

                FilterStyle().resetPreviousFilterStyle(requireContext(),selectMainFilter)

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
        mainFilterViewModel.mainFilterDataList.observe(viewLifecycleOwner, Observer { mainFilterData ->

            mainFilterAdapter.submitList(mainFilterData)
//            productDataViewModel.setCurrentMainFilterData(mainFilterData)
            dbViewModel.setCurrentMainFilterData(mainFilterData)
            dbViewModel.loadFavoriteProductDataByPaging()
        })

    }

    private fun observeFilterDataViewModel() {
        filterDataViewModel.filterDataList.observe(viewLifecycleOwner, Observer { data ->
            productFilterAdapter.submitList(data)

        })
        filterDataViewModel.filterBar.observe(viewLifecycleOwner, Observer { isVisible  ->
//            FilterAnimated().viewAnimated(isVisible,binding.filterBarDetail)

        })

        filterDataViewModel.selectFilterColorSwitch.observe(viewLifecycleOwner, Observer { switchState ->

            if(switchState){
                FilterStyle().updateFilterStyle(requireContext(),selectMainFilter)
            }
            else{
                FilterStyle().resetPreviousFilterStyle(requireContext(),selectMainFilter)
            }
        })
    }

    //todo 즐겨찾기 페이지 만들기
    private fun observeProductDataViewModel() {

        productDataViewModel.clickProductData.observe(viewLifecycleOwner, Observer { clickProductData ->
            showProductDetailDialog(clickProductData)
        })

        productDataViewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            dbViewModel.favoriteProductJudgment(isFavorite)
        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showProductDetailDialog(productData: ProductData) {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.product_detail_viewer, null)
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
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)


        val dialog = mBuilder.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setOnDismissListener {
            productItemRecyclerAdapter.notifyDataSetChanged()
        }
    }
    private fun observeDataBaseViewModel() {
        dbViewModel.favoriteProducts.observe(viewLifecycleOwner, Observer { favoriteProductData ->

        })


        dbViewModel.favoriteProductByPaging.observe(viewLifecycleOwner, Observer { favoriteProductByPaging ->
            lifecycleScope.launch {

                favoriteProductByPaging.collectLatest { pagingData ->


                    val transformedData = pagingData
                        .map { productData ->
                            Log.d("productData", productData.toString())
                            productDataViewModel.convertSingleProductDataType(productData)
                        }
//                        .filter {
//                            productDataViewModel.loadFilteredProductData(it)
//                        }

                    productItemRecyclerAdapter.submitData(lifecycle, transformedData)

                }

            }
        })


    }
}