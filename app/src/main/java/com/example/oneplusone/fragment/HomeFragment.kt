package com.example.oneplusone.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.util.DialogBuilder
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.util.FilterAnimated
import com.example.oneplusone.util.FilterStyle
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

    private val productSpacingController = ItemSpacingController(25, 25, 40)

    private val filterSpacingController = ItemSpacingController(15, 15, 0)

    private var selectMainFilter:View?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


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
            override fun onMainFilterClick(mainFilter: MainFilterData,itemView: View) {


                //하나의 메인 필터를 터치한 상태에서 다른 메인필터를 터치하면 초기화
                FilterStyle().resetPreviousFilterStyle(requireContext(),selectMainFilter)

                selectMainFilter = itemView
                filterDataViewModel.showFilter(mainFilter.filterType)
//                Log.d("mainFilter", mainFilter.toString())
            }
        })
        binding.mainFilterViewer.adapter = mainFilterAdapter
//        binding.mainFilterViewer.addItemDecoration(filterSpacingController)
    }

    private fun initProductFilterAdapter() {
        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {

            override fun onFilterClick(filterData: FilterData) {

//                val currentMainFilter=mainFilterAdapter.updateFilterItem(filterData)

                mainFilterViewModel.updateMainFilter(filterData)
//
//                Log.d("mainFilterAdapter", currentMainFilter.toString())
//                mainFilterViewModel.currentMainFilterUpdate(currentMainFilter)
                //세부 필터를 고르면 불러온 데이터를 제거함
                filterDataViewModel.clearFilterData()

            }

        })
        binding.filterViewer.adapter = productFilterAdapter
    }

    private fun initProductItemRecyclerAdapter() {
        productItemRecyclerAdapter = ProductItemRecyclerAdapter(object : ProductClickListener {
            override fun onItemClick(productData: ProductData) {

                productDataViewModel.loadClickProductData(productData)
            }
        })
        binding.productGridView.adapter = productItemRecyclerAdapter
        binding.productGridView.addItemDecoration(productSpacingController)
    }

    private fun observeMainFilterViewModel() {
        mainFilterViewModel.mainFilterDataList.observe(viewLifecycleOwner, Observer { data ->

            mainFilterAdapter.submitList(data)
            productDataViewModel.loadFilteringProductData(data)
        })
    }

    private fun observeFilterDataViewModel() {
        //여기서 변화를 감지하고 변경함
        filterDataViewModel.filterDataList.observe(viewLifecycleOwner, Observer { data ->
            productFilterAdapter.submitList(data)

        })

        filterDataViewModel.filterBar.observe(viewLifecycleOwner, Observer { isVisible  ->
            //사라질 때 시각적으로 버벅 거림이 느껴져서 애니메이션으로 부드럽게 바꿨음
            FilterAnimated().viewAnimated(isVisible,binding.filterBarDetail)

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

    private fun observeProductDataViewModel() {
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { data ->
            productItemRecyclerAdapter.submitList(data)
        })

        productDataViewModel.clickProductData.observe(viewLifecycleOwner, Observer { clickProductData ->
            DialogBuilder().showProductDetailDialog(requireContext(), clickProductData)
        })
        productDataViewModel.filterProductData.observe(viewLifecycleOwner, Observer { filterProductData ->
            Log.d("filterProductData", filterProductData.toString())
        })
    }
}
