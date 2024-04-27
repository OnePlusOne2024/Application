package com.example.oneplusone.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentMapBinding
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.model.data.ProductData
import com.example.oneplusone.model.data.enums.BenefitsType
import com.example.oneplusone.model.data.enums.ConvenienceType
import com.example.oneplusone.model.data.enums.FilterType
import com.example.oneplusone.model.data.enums.ProductCategoryType
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.util.DialogBuilder
import com.example.oneplusone.util.FilterAnimated
import com.example.oneplusone.util.FilterStyle
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.MainFilterViewModel
import com.example.oneplusone.viewModel.MapDataViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var binding: FragmentMapBinding

    private var googleMap: GoogleMap? = null

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val filterDataViewModel: FilterDataViewModel by viewModels()
    private val mainFilterViewModel: MainFilterViewModel by viewModels()
    private val mapDataViewModel: MapDataViewModel by viewModels()

    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
    private lateinit var mainFilterAdapter: MainFilterRecyclerAdapter

    private val productSpacingController = ItemSpacingController(25, 25, 40)

    @SuppressLint("InflateParams")
    private val markerView: View =LayoutInflater.from(requireContext()).inflate(R.layout.custom_marker,null)
    private val tagMarker: TextView = markerView.findViewById(R.id.custom_marker)

    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setupDataBinding()
        observeSetting()
        mapZipperTouch()

        //todo:지도에 편의점 띄우기 + 터치시 상품뷰 출력

        //임시로 초기 상품뷰의 높이 설정
        binding.mapProductLayout.layoutParams.height=(screenHeight * 0.4).toInt()
    }

    private fun initAdapter() {
        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
    }
//

    //레이아웃과 연결 (Hilt)
    private fun setupDataBinding() {
        binding.apply {
            mainFilterViewModel = this@MapFragment.mainFilterViewModel
            filterDataViewModel = this@MapFragment.filterDataViewModel
            productDataViewModel = this@MapFragment.productDataViewModel
            mapDataViewModel = this@MapFragment.mapDataViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
    private fun observeSetting(){
        observeMainFilterViewModel()
        observeFilterDataViewModel()
        observeProductDataViewModel()
        observeConvenienceData()
    }



    //지도에서 지퍼로 올리거나 내리거나 할 수 있는 기능
    @SuppressLint("ClickableViewAccessibility")
    private fun mapZipperTouch() {
        var initialTouchY = 0f
        var initialHeight = 0

        binding.mapZipper.setOnTouchListener { _, event ->
            when (event.action) {
                //터치 했을 때
                MotionEvent.ACTION_DOWN -> {
                    initialTouchY = event.rawY
                    initialHeight = binding.mapProductLayout.layoutParams.height
                    Log.d("initialHeight", initialHeight.toString())
                    true
                }
                //움직일 때
                MotionEvent.ACTION_MOVE -> {
                    productDataViewModel.updateLayoutHeight(initialHeight, initialTouchY, event.rawY,screenHeight)
                    true
                }

                else -> false
            }
        }
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

        productDataViewModel.layoutHeight.observe(viewLifecycleOwner, Observer { height ->

            val params=binding.mapProductLayout.layoutParams

            params.height=height

            binding.mapProductLayout.layoutParams = params

        })
    }

    @SuppressLint("InflateParams")
    private fun observeConvenienceData() {
        mapDataViewModel.convenienceDataList.observe(viewLifecycleOwner, Observer { convenienceData ->
            for(i in convenienceData.indices){
                addMarker(convenienceData[i])
            }
        })
    }

    private fun addMarker(convenienceData: ConvenienceData): Marker {
        var addressList: List<Address>? = null
        var markerOptions = MarkerOptions()

        markerOptions.position(convenienceData.conveniencePosition)
        tagMarker.text = convenienceData.convenienceName
        markerOptions.icon(
            createDrawableFromView(requireContext(), convenienceData.convenienceType)
            )
        )
        return mMap.addMarker(markerOptions)
    }
    private fun createDrawableFromView(context: Context, convenienceType: String): Drawable {


        val convenienceImageBitmap:Bitmap=when(convenienceType){

            ConvenienceType.STORE_GS_25.title -> (ContextCompat.getDrawable(context, R.drawable.gs25_product_icon) as BitmapDrawable).bitmap
            ConvenienceType.STORE_CU.title -> (ContextCompat.getDrawable(context, R.drawable.cu_product_icon) as BitmapDrawable).bitmap
            ConvenienceType.STORE_SEVEN_ELEVEN.title -> (ContextCompat.getDrawable(context, R.drawable.seven_eleven_product_icon) as BitmapDrawable).bitmap
            ConvenienceType.STORE_E_MART24.title -> (ContextCompat.getDrawable(context, R.drawable.emart24_product_icon) as BitmapDrawable).bitmap

            else -> (ContextCompat.getDrawable(context, R.drawable.all_convenience_store) as BitmapDrawable).bitmap
        }

        return BitmapDrawable(context.resources,convenienceImageBitmap)
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