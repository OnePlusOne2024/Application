package com.example.oneplusone.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oneplusone.R
import com.example.oneplusone.databinding.FragmentMapBinding
import com.example.oneplusone.model.ProductDataViewModel
import com.example.oneplusone.model.ProductItemRecyclerAdapter
import com.example.oneplusone.util.ItemSpacingController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding

    private var googleMap: GoogleMap? = null
    private lateinit var productDataViewModel: ProductDataViewModel

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

        val itemSpacingController = ItemSpacingController(25, 25, 40)

        productDataViewModel = ViewModelProvider(this)[ProductDataViewModel::class.java]

        val productClickListener = ProductItemRecyclerAdapter.ProductClickListener {
        }

        val productItemRecyclerAdapter = ProductItemRecyclerAdapter(productClickListener)

        binding.mapProductGridView.adapter = productItemRecyclerAdapter
        binding.mapProductGridView.layoutManager = GridLayoutManager(context, 2)
        binding.mapProductGridView.addItemDecoration(itemSpacingController)

        //주의 사항: Fragment에서 DataBinding을 사용할 경우, lifecycleOwner에 this(Fragment)가 아닌 viewLifecycleOwner를 전달해야 한다.(누수 방지)
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { productDataList ->
            Log.d("test", productDataViewModel.productDataList.toString())
            productItemRecyclerAdapter.submitList(productDataList)
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