package com.example.oneplusone.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentMapBinding
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
import com.example.oneplusone.util.FilterAnimated
import com.example.oneplusone.util.FilterStyle
import com.example.oneplusone.viewModel.ProductDataViewModel
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.MainFilterViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var binding: FragmentMapBinding

    private var googleMap: GoogleMap? = null

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
        binding = FragmentMapBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        setupDataBinding()
//
        observeSetting()
    }

    private fun initAdapter() {
        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
    }
//

    //Hilt
    private fun setupDataBinding() {
        binding.apply {
            mainFilterViewModel = this@MapFragment.mainFilterViewModel
            filterDataViewModel = this@MapFragment.filterDataViewModel
            productDataViewModel = this@MapFragment.productDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
    private fun observeSetting(){
        observeMainFilterViewModel()
        observeFilterDataViewModel()
        observeProductDataViewModel()
    }

    private fun mapZipperTouch(){

    }
    private fun initMainFilterAdapter() {
        mainFilterAdapter = MainFilterRecyclerAdapter(object : MainFilterClickListener {
            override fun onMainFilterClick(mainFilter: MainFilterData,itemView: View) {


                //하나의 메인 필터를 터치한 상태에서 다른 메인필터를 터치하면 초기화
                FilterStyle().resetPreviousFilterStyle(requireContext(),selectMainFilter)

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
        binding.mapProductGridView.adapter = productItemRecyclerAdapter
        binding.mapProductGridView.addItemDecoration(productSpacingController)
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val point = LatLng(37.514655, 126.979974)
        googleMap.addMarker(MarkerOptions().position(point))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12f))
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
}