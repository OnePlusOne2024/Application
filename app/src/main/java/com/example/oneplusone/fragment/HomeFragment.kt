package com.example.oneplusone.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var productDataViewModel: ProductDataViewModel
    private lateinit var filterDataViewModel: FilterDataViewModel
    private lateinit var mainFilterViewModel: MainFilterViewModel

    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
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

        filterDataViewModel = ViewModelProvider(this)[FilterDataViewModel::class.java]
        productDataViewModel = ViewModelProvider(this)[ProductDataViewModel::class.java]

        initAdapter()

        binding.apply {
            this.filterDataViewModel = this@HomeFragment.filterDataViewModel
            this.productDataViewModel = this@HomeFragment.productDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        observeViewModel()

    }

    private fun initAdapter() {

        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {
            override fun onFilterClick(filterData: FilterData) {
                when(filterData.filterType){
                    FilterType.CONVENIENCE -> {
                        binding.filterItem1.setImageResource(filterData.filterImage)
                        binding.filterBarItemText1.text=filterData.filterText
                    }
                    FilterType.PRODUCT_CATEGORY -> {
                        binding.filterItem2.setImageResource(filterData.filterImage)
                        binding.filterBarItemText2.text=filterData.filterText
                    }
                    FilterType.BENEFITS -> {
                        binding.filterItem3.setImageResource(filterData.filterImage)
                        binding.filterBarItemText3.text=filterData.filterText
                    }
                }
            }
        })
        binding.filterViewer.adapter = productFilterAdapter

        productItemRecyclerAdapter = ProductItemRecyclerAdapter(object : ProductClickListener {
            override fun onItemClick(productData: ProductData) {

            }
        })
        binding.productGridView.adapter = productItemRecyclerAdapter
    }

    private fun observeViewModel() {
        filterDataViewModel.filterDataList.observe(viewLifecycleOwner, Observer { data ->
            productFilterAdapter.submitList(data)
        })

        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { data ->
            productItemRecyclerAdapter.submitList(data)
        })
    }
}
