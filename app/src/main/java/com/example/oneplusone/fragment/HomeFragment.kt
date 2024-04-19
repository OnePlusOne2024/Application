package com.example.oneplusone.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enum.FilterType
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.MainFilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val filterDataViewModel: FilterDataViewModel by viewModels()
    private val mainFilterViewModel: MainFilterViewModel by viewModels()

    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
    private lateinit var mainFilterAdapter: MainFilterRecyclerAdapter
    private var filterBarSwitch:Boolean = false

    private lateinit var currentFilterCategory:FilterType
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productSpacingController = ItemSpacingController(25, 25, 40)
        val filterSpacingController = ItemSpacingController(25, 25, 0)

        initAdapter()

        setupDataBinding()

        observeSetting()

    }

    private fun initAdapter() {
        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
    }

    private fun setupDataBinding() {
        binding.apply {
            mainFilterViewModel = this@HomeFragment.mainFilterViewModel
            filterDataViewModel = this@HomeFragment.filterDataViewModel
            productDataViewModel = this@HomeFragment.productDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
    private fun observeSetting(){
        observeMainFilterViewModel()
        observeFilterDataViewModel()
        observeProductDataViewModel()
    }

    private fun initMainFilterAdapter() {
        mainFilterAdapter = MainFilterRecyclerAdapter(object : MainFilterClickListener {
            override fun onMainFilterClick(mainFilter: MainFilterData) {

//                filterDataViewModel.filterBarSwitch()

                when (mainFilter.filterType) {
                    FilterType.CONVENIENCE -> filterDataViewModel.loadItems(FilterType.CONVENIENCE)
                    FilterType.PRODUCT_CATEGORY -> filterDataViewModel.loadItems(FilterType.PRODUCT_CATEGORY)
                    FilterType.BENEFITS -> filterDataViewModel.loadItems(FilterType.BENEFITS)
                }
            }
        })
        binding.mainFilterViewer.adapter = mainFilterAdapter
    }


    private fun initProductFilterAdapter() {
        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {

            override fun onFilterClick(filterData: FilterData) {

//                filterDataViewModel.filterBarSwitch()

                when (filterData.filterType) {
                    FilterType.CONVENIENCE -> {
                        mainFilterAdapter.updateFilterItem(FilterType.CONVENIENCE, filterData.filterImage, filterData.filterText)
                    }
                    FilterType.PRODUCT_CATEGORY -> {
                        mainFilterAdapter.updateFilterItem(FilterType.PRODUCT_CATEGORY, filterData.filterImage, filterData.filterText)
                    }
                    FilterType.BENEFITS -> {
                        mainFilterAdapter.updateFilterItem(FilterType.BENEFITS, filterData.filterImage, filterData.filterText)
                    }
                }
                //세부 필터를 고르면 불러온 데이터를 제거함
                filterDataViewModel.clearFilterData()

            }
        })
        binding.filterViewer.adapter = productFilterAdapter
    }

    private fun initProductItemRecyclerAdapter() {
        productItemRecyclerAdapter = ProductItemRecyclerAdapter(object : ProductClickListener {
            override fun onItemClick(productData: ProductData) {

            }
        })
        binding.productGridView.adapter = productItemRecyclerAdapter
    }



    private fun observeMainFilterViewModel() {
        mainFilterViewModel.mainFilterDataList.observe(viewLifecycleOwner, Observer { data ->
            mainFilterAdapter.submitList(data)
        })

    }

    private fun observeFilterDataViewModel() {
        //여기서 변화를 감지하고 변경함
        filterDataViewModel.filterDataList.observe(viewLifecycleOwner, Observer { data ->
            productFilterAdapter.submitList(data)
        })
        filterDataViewModel.filterBar.observe(viewLifecycleOwner, Observer { isVisible  ->

            //사라질 때 시각적으로 버벅 거림이 느껴져서 애니메이션으로 부드럽게 바꿨음
            if (isVisible) {
                binding.filterBarDetail.apply {
                    alpha = 0f
                    visibility = View.VISIBLE
                    animate()
                        .alpha(1f)
                        .setDuration(300)
                        .setListener(null)
                }
            } else {
                binding.filterBarDetail.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            binding.filterBarDetail.visibility = View.GONE
                        }
                    })
            }
        })
    }

    private fun observeProductDataViewModel() {
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { data ->
            productItemRecyclerAdapter.submitList(data)
        })
    }

}
