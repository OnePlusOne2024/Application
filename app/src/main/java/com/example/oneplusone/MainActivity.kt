package com.example.oneplusone


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oneplusone.databinding.ActivityMainBinding
import com.example.oneplusone.model.ProductData
import com.example.oneplusone.model.ProductDataViewModel
import com.example.oneplusone.model.ProductItemRecyclerAdapter
import com.example.oneplusone.util.ItemSpacingController

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val itemSpacingController = ItemSpacingController(30,30,40)

        val productDataViewModel= ViewModelProvider(this)[ProductDataViewModel::class.java]

        val productClickListener=ProductItemRecyclerAdapter.ProductClickListener{
        }

        val productItemRecyclerAdapter=ProductItemRecyclerAdapter(productClickListener)

        binding.productGridView.adapter=productItemRecyclerAdapter

        binding.productGridView.layoutManager=GridLayoutManager(this,2)
        binding.productGridView.addItemDecoration(itemSpacingController)

        productDataViewModel.productDataList.observe(this, Observer {productDataList->
            Log.d("test", productDataViewModel.productDataList.toString())
            productItemRecyclerAdapter.submitList(productDataList)
        })
    }


}