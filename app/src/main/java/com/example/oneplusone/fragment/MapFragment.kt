package com.example.oneplusone.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.paging.map
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentMapBinding
import com.example.oneplusone.databinding.ProductDetailViewerBinding
import com.example.oneplusone.db.ProductData
import com.example.oneplusone.`interface`.FilterClickListener
import com.example.oneplusone.`interface`.MainFilterClickListener
import com.example.oneplusone.`interface`.ProductClickListener
import com.example.oneplusone.`interface`.ProductFavoriteClickListener
import com.example.oneplusone.model.data.ConvenienceData
import com.example.oneplusone.model.data.FilterData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.recyclerAdapter.MainFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductFilterRecyclerAdapter
import com.example.oneplusone.recyclerAdapter.ProductItemRecyclerAdapter
import com.example.oneplusone.util.CustomMarker
import com.example.oneplusone.util.FilterAnimated
import com.example.oneplusone.util.FilterStyle
import com.example.oneplusone.util.ItemSpacingController
import com.example.oneplusone.viewModel.DataBaseViewModel
import com.example.oneplusone.viewModel.FilterDataViewModel
import com.example.oneplusone.viewModel.MapDataViewModel
import com.example.oneplusone.viewModel.MapMainFilterViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.Manifest
import android.os.Looper
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var binding: FragmentMapBinding

    private var googleMap: GoogleMap? = null

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val filterDataViewModel: FilterDataViewModel by viewModels()
    private val mapMainFilterViewModel: MapMainFilterViewModel by viewModels()
    private val mapDataViewModel: MapDataViewModel by viewModels()
    private val dbViewModel: DataBaseViewModel by viewModels()

    private lateinit var productFilterAdapter: ProductFilterRecyclerAdapter
    private lateinit var productItemRecyclerAdapter: ProductItemRecyclerAdapter
    private lateinit var mainFilterAdapter: MainFilterRecyclerAdapter


    private val productSpacingController = ItemSpacingController(25, 25, 40)

    lateinit var locationPermission: ActivityResultLauncher<Array<String>>

    //위치 서비스가 gps를 사용해서 위치를 확인
    lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationCallback: LocationCallback


//todo 코드 너무 중구난방으로 나누어져 있음, 맵메인뷰모델을 꼭 써야하는지 생각해보기, 디자인 구림,
// 점포 아이콘 아래에 정확한 위치에 편의점 아이콘을 넣는것은 어떨지 생각해보기


    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    private var selectMainFilter:View?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,


    ): View {


        binding = FragmentMapBinding.inflate(inflater, container, false)

//        @SuppressLint("InflateParams")
//        markerView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_marker, null)
//        tagMarker = markerView.findViewById(R.id.custom_marker)

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
//        onMarkerClickListener()

        locationPermissionCheck()
        locationPermissionLaunch()
        //todo:지도에 편의점 띄우기 + 터치시 상품뷰 출력
        dbViewModel.loadFavoriteProducts()

        //임시로 초기 상품뷰의 높이 설정
        binding.mapProductLayout.layoutParams.height=(screenHeight * 0.4).toInt()
    }



    private fun locationPermissionLaunch() {
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun locationPermissionCheck() {
        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){ results ->
            if(results.all{it.value}){

            }else{ //문제가 발생했을 때
                Toast.makeText(requireContext(),"권한 승인이 필요합니다.",Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun initAdapter() {

        initMainFilterAdapter()
        initProductFilterAdapter()
        initProductItemRecyclerAdapter()
        productItemRecyclerAdapterStateManagement()
    }
//




    //레이아웃과 연결 (Hilt)
    private fun setupDataBinding() {
        binding.apply {
            mapMainFilterViewModel = this@MapFragment.mapMainFilterViewModel
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
        observeDataBaseViewModel()
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

//                mainFilterAdapter.updateFilterItem(filterData)
                mapMainFilterViewModel.updateMainFilter(filterData)
                //세부 필터를 고르면 불러온 데이터를 제거함
                filterDataViewModel.clearFilterData()

            }
        })
        binding.filterViewer.adapter = productFilterAdapter
//        binding.filterViewer.addItemDecoration(filterSpacingController)
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
        binding.mapProductGridView.adapter = productItemRecyclerAdapter
        binding.mapProductGridView.addItemDecoration(productSpacingController)
    }

    private fun productItemRecyclerAdapterStateManagement(){
        productItemRecyclerAdapter.addLoadStateListener { combinedLoadStates ->
            if(combinedLoadStates.append.endOfPaginationReached) {

                if(productItemRecyclerAdapter.itemCount < 1) {
                    binding.emptyProduct.visibility = View.VISIBLE
                }else {
                    binding.emptyProduct.visibility = View.GONE
                }
            }
        }
    }


    private fun observeMainFilterViewModel() {
        mapMainFilterViewModel.mainFilterDataList.observe(viewLifecycleOwner, Observer { mainFilterData ->
            mainFilterAdapter.submitList(mainFilterData)
//            productDataViewModel.setCurrentMainFilterData(mainFilterData)

            dbViewModel.setCurrentMainFilterData(mainFilterData)
//            dbViewModel.loadProductDataList()

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

        productDataViewModel.clickProductData.observe(viewLifecycleOwner, Observer { clickProductData ->

            showProductDetailDialog(clickProductData)
        })

        productDataViewModel.layoutHeight.observe(viewLifecycleOwner, Observer { height ->

            val params=binding.mapProductLayout.layoutParams

            params.height=height

            binding.mapProductLayout.layoutParams = params

        })


        productDataViewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            dbViewModel.favoriteProductJudgment(isFavorite)
        })

        productDataViewModel.userCoordinate.observe(viewLifecycleOwner, Observer { userCoordinate ->

            //업데이트된 좌표로 카메라를 이동시키고 서버에서 편의점 리스트를 불러옴
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userCoordinate.latitude, userCoordinate.longitude), 15f))

            mapDataViewModel.loadConvenienceDataFromServer(userCoordinate)
        })

    }



    @SuppressLint("NotifyDataSetChanged")
    private fun showProductDetailDialog(productData: ProductData) {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.product_detail_viewer, null)
        val dialogBinding = ProductDetailViewerBinding.bind(mDialogView)

        //어쩔 수 없이 notifyItemChanged로 업데이트 하기로 결정
//        val index = productItemRecyclerAdapter.currentList.indexOfFirst { it.id == productData.id }

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

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun observeConvenienceData() {
        mapDataViewModel.convenienceDataList.observe(viewLifecycleOwner, Observer { convenienceData ->

            if (convenienceData != null) {
                if (convenienceData.isEmpty()) {
                    Toast.makeText(requireContext(), "500M 이내에 편의점이 존재하지 않습니다.", Toast.LENGTH_LONG).show()
                } else {
                    for (i in convenienceData.indices) {
                        addMarker(convenienceData[i], false)
                    }
                    Toast.makeText(requireContext(),"500M 이내의 편의점을 불러왔습니다.",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "편의점 정보를 가져오는데 실패했습니다.", Toast.LENGTH_LONG).show()
            }
        })

        mapDataViewModel.selectedMarkerData.observe(viewLifecycleOwner, Observer { selectedMarkerData ->
            Log.d("selectedMarkerData", selectedMarkerData.toString())
            if(selectedMarkerData!=null){
                binding.mapZipper.visibility=View.VISIBLE
                binding.mapProductLayout.visibility=View.VISIBLE
                binding.productFilter.text=(selectedMarkerData.convenienceName)

                //선택된 편의점의 종류에 맞게 상품 로드
                dbViewModel.setConvenienceType(selectedMarkerData.convenienceType)
                //메인필터를 초기화
                mapMainFilterViewModel.initMainFilters()
            }else{
                binding.mapZipper.visibility=View.GONE
                binding.mapProductLayout.visibility=View.GONE
            }

            googleMap?.clear()
            mapDataViewModel.convenienceDataList.value?.forEach { convenienceData ->

                val isSelected = convenienceData == selectedMarkerData
                addMarker(convenienceData, isSelected)
            }
        })



    }

    private fun addMarker(convenienceData: ConvenienceData, isSelected: Boolean) {

        val markerOptions = MarkerOptions()
        markerOptions.position(convenienceData.conveniencePosition)
        val icon = BitmapDescriptorFactory.fromBitmap(CustomMarker().createDrawableFromView(requireContext(), convenienceData, isSelected))
        markerOptions.icon(icon)
        val marker = googleMap?.addMarker(markerOptions)
        marker?.tag = convenienceData
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
//        val point = LatLng(37.514655, 126.979974)
//        googleMap.addMarker(MarkerOptions().position(point))
        updateLocation()
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.514655, 126.979974), 12f))
        observeConvenienceData()
        setMarkerClickListener()
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(){

        //intervalMillis는 업데이트 단위 1초
        //setMinUpdateDistanceMeters는 거리 변화의 업데이트 1000F는 1키로
        //setWaitForAccurateLocation는 정확한 위치를 기다릴리 여부
        val locationRequest=LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000).apply {
            setMinUpdateDistanceMeters(500f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if(location==null){
                        Toast.makeText(requireContext(),"사용자의 위치를 가져오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show()
                    }
                    productDataViewModel.setCoordinate(location.latitude,location.longitude)
                    Log.d("위치정보", "위도: ${location.latitude} 경도: ${location.longitude}")

                }
            }
        }

        // 권한 처리 후
        LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    //이상하게 일정거리 이상에서 마커를 터치하면 분명 같은 마커를 터치 했는데 다른 마커가 터치된 것처럼 반응함(축소거리 조정예정)
    private fun setMarkerClickListener() {
        googleMap?.setOnMarkerClickListener { marker ->
            val convenienceData = marker.tag as? ConvenienceData
            mapDataViewModel.selectMarker(convenienceData)
            true
        }
    }

    private fun observeDataBaseViewModel() {
        dbViewModel.favoriteProducts.observe(viewLifecycleOwner, Observer { favoriteProductData ->
            productDataViewModel.loadFavoriteProduct(favoriteProductData)
        })

        dbViewModel.convenienceType.observe(viewLifecycleOwner, Observer { convenienceType ->

        })

        dbViewModel.DBProductDataList.observe(viewLifecycleOwner, Observer { dbProductDataList ->

            lifecycleScope.launch {

                dbProductDataList.collectLatest { pagingData ->

                    val transformedData = pagingData
                        .map { productData ->

                            productDataViewModel.isProductFavorite(productData)
                        }

                    productItemRecyclerAdapter.submitData(lifecycle, transformedData)

                }

            }
        })

        dbViewModel.mapMergeData.observe(viewLifecycleOwner, Observer { (mainFilterDataList, convenienceType) ->
            Log.d("mainFilterDataList", mainFilterDataList.toString())
            Log.d("mainFilterDataList", convenienceType.toString())
            if (mainFilterDataList != null && convenienceType !=null) {
                dbViewModel.loadFavoriteProducts()
                dbViewModel.loadProductDataByConvenienceType()
            }

        })
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
        //위치정보 획득 권한 요청

        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
}