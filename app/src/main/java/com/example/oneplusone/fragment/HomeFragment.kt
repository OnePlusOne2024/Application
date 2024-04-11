package com.example.oneplusone.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oneplusone.MainActivity
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.model.FilterDataViewModel
import com.example.oneplusone.model.ProductDataViewModel
import com.example.oneplusone.model.ProductFilterRecyclerAdapter
import com.example.oneplusone.model.ProductItemRecyclerAdapter
import com.example.oneplusone.util.ItemSpacingController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productDataViewModel: ProductDataViewModel
    private val viewModel: FilterDataViewModel by viewModels()





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
        val filterSpacingController = ItemSpacingController(25, 25,0)

        productDataViewModel = ViewModelProvider(this)[ProductDataViewModel::class.java]

        val productClickListener = ProductItemRecyclerAdapter.ProductClickListener {
        }

        val productItemRecyclerAdapter = ProductItemRecyclerAdapter(productClickListener)

        binding.productGridView.adapter = productItemRecyclerAdapter
        binding.productGridView.layoutManager = GridLayoutManager(context, 2)
        binding.productGridView.addItemDecoration(productSpacingController)

        //주의 사항: Fragment에서 DataBinding을 사용할 경우, lifecycleOwner에 this(Fragment)가 아닌 viewLifecycleOwner를 전달해야 한다.(누수 방지)
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { productDataList ->
            Log.d("test", productDataViewModel.productDataList.toString())
            productItemRecyclerAdapter.submitList(productDataList)
        })

        binding.apply {
            lifecycleOwner=viewLifecycleOwner
            filterDataViewModel=viewModel
        }

//        val filterClickListener = ProductFilterRecyclerAdapter.FilterClickListener {
//        }

//        val filterItemRecyclerAdapter = ProductFilterRecyclerAdapter(filterClickListener)
//        binding.filterViewer.adapter = filterItemRecyclerAdapter
//        binding.filterViewer.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
//
//        filterDataViewModel = ViewModelProvider(this)[FilterDataViewModel::class.java]
//        filterDataViewModel.filterDataList.observe(viewLifecycleOwner, Observer { filterDataList ->
//            filterItemRecyclerAdapter.submitList(filterDataList)
//        })

//        setupFilterClickListeners()
    }

//    private fun setupFilterClickListeners() {
//        binding.convenienceStoreFilter.setOnClickListener {
//            filterDataViewModel.loadItems(CONVENIENCE_STORE)
//        }
//        binding.productCategoryFilter.setOnClickListener {
//            filterDataViewModel.loadItems(PRODUCT_CATEGORY)
//        }
//        binding.productBenefitsFilter.setOnClickListener {
//            filterDataViewModel.loadItems(BENEFITS)
//        }
//    }
//    companion object {
//        const val CONVENIENCE_STORE: String="convenienceStore"
//        const val PRODUCT_CATEGORY: String="productCategory"
//        const val BENEFITS: String="benefits"
//    }
}
