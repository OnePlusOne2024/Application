package com.example.oneplusone.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oneplusone.MainActivity
import com.example.oneplusone.databinding.FragmentHomeBinding
import com.example.oneplusone.model.ProductDataViewModel
import com.example.oneplusone.model.ProductItemRecyclerAdapter
import com.example.oneplusone.util.ItemSpacingController


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productDataViewModel: ProductDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemSpacingController = ItemSpacingController(25, 25, 40)

        productDataViewModel = ViewModelProvider(this)[ProductDataViewModel::class.java]

        val productClickListener = ProductItemRecyclerAdapter.ProductClickListener {
        }

        val productItemRecyclerAdapter = ProductItemRecyclerAdapter(productClickListener)

        binding.productGridView.adapter = productItemRecyclerAdapter
        binding.productGridView.layoutManager = GridLayoutManager(context, 2)
        binding.productGridView.addItemDecoration(itemSpacingController)

        //주의 사항: Fragment에서 DataBinding을 사용할 경우, lifecycleOwner에 this(Fragment)가 아닌 viewLifecycleOwner를 전달해야 한다.(누수 방지)
        productDataViewModel.productDataList.observe(viewLifecycleOwner, Observer { productDataList ->
            Log.d("test", productDataViewModel.productDataList.toString())
            productItemRecyclerAdapter.submitList(productDataList)
        })
    }
}