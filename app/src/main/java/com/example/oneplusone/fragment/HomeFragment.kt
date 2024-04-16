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
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
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

        mainFilterViewModel = ViewModelProvider(this)[MainFilterViewModel::class.java]
        filterDataViewModel = ViewModelProvider(this)[FilterDataViewModel::class.java]
        productDataViewModel = ViewModelProvider(this)[ProductDataViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner


        binding.apply {
            this.mainFilterViewModel = this@HomeFragment.mainFilterViewModel
            this.filterDataViewModel = this@HomeFragment.filterDataViewModel
            this.productDataViewModel = this@HomeFragment.productDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

}
