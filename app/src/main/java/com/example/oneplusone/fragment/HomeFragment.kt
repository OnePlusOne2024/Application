package com.example.oneplusone.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enums.FilterType
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

    private val productSpacingController = ItemSpacingController(25, 25, 40)

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
            override fun onMainFilterClick(mainFilter: MainFilterData,itemView: View) {

//                resetPreviousMainFilter()

                filterDataViewModel.setFilterType(mainFilter.filterType)

//                updateMainFilterUI(itemView)

//                // 현재 선택된 뷰를 기록
//                selectMainFilter = itemView
            }
        })
        binding.mainFilterViewer.adapter = mainFilterAdapter
    }

    private fun resetPreviousMainFilter() {
        selectMainFilter?.let {
            it.alpha = 1f
            it.backgroundTintList = null
            it.findViewById<TextView>(R.id.main_filter_text).setTextColor(Color.parseColor("#000000"))
        }
    }

    private fun updateMainFilterUI(itemView: View) {
        itemView.alpha = 0.5f
        itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#87CEEB"))
        itemView.findViewById<TextView>(R.id.main_filter_text).setTextColor(Color.parseColor("#04A7EA"))
    }

    private fun initProductFilterAdapter() {
        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {

            override fun onFilterClick(filterData: FilterData) {

//                filterDataViewModel.filterBarSwitch()

                mainFilterAdapter.updateFilterItem(filterData)

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
        binding.productGridView.addItemDecoration(productSpacingController)
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
