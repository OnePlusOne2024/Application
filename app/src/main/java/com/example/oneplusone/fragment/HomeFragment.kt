package com.example.oneplusone.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.databinding.ProductDetailViewerBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
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


                if(selectMainFilter!=itemView){
                    resetPreviousFilterStyle()
                }
                selectMainFilter = itemView
                filterDataViewModel.showFilter(mainFilter.filterType)


            }
        })
        binding.mainFilterViewer.adapter = mainFilterAdapter
//        binding.mainFilterViewer.addItemDecoration(filterSpacingController)
    }



    private fun initProductFilterAdapter() {
        productFilterAdapter = ProductFilterRecyclerAdapter(object : FilterClickListener {

            override fun onFilterClick(filterData: FilterData) {



                mainFilterAdapter.updateFilterItem(filterData)

                //세부 필터를 고르면 불러온 데이터를 제거함
                filterDataViewModel.clearFilterData()

            }
        })
        binding.filterViewer.adapter = productFilterAdapter
//        binding.filterViewer.addItemDecoration(filterSpacingController)
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
        filterDataViewModel.selectFilterColorSwitch.observe(viewLifecycleOwner, Observer { switchState ->

            if(switchState){
                updateFilterStyle()
            }else{
                resetPreviousFilterStyle()
            }
        })
    }

    private fun observeProductDataViewModel() {
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { data ->
            productItemRecyclerAdapter.submitList(data)
        })

        productDataViewModel.clickProductData.observe(viewLifecycleOwner, Observer { clickProductData ->

            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.product_detail_viewer, null)

            //프로덕트 아이템과 바인딩 어댑터를 공유함. 프로덕트 아이템은 리사이클러 어댑터 에서 바인딩 어댑터와 연결하지만 이건 여기서 연결 했음 근데 조금 꼴보기 싫어서 수정할 예정
            val dialogBinding = ProductDetailViewerBinding.bind(mDialogView)
            dialogBinding.productData = clickProductData

            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)

            val dialog = mBuilder.show()

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        })
    }

    private fun resetPreviousFilterStyle() {
        selectMainFilter?.let {
            it.alpha = 1f
            it.backgroundTintList = null
//            it.setBackgroundColor(Color.parseColor("#ffffff"))
            it.findViewById<TextView>(R.id.main_filter_text).setTextColor(
                ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun updateFilterStyle() {
        selectMainFilter?.let {
            it.alpha = 0.5f
            it.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.filter_background_selected)
            )
//            it.setBackgroundColor(Color.parseColor("#87CEEB"))
            it.findViewById<TextView>(R.id.main_filter_text).setTextColor(
                ContextCompat.getColor(requireContext(), R.color.filter_text_selected))
        }
    }

}
