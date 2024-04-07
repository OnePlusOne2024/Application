package com.example.oneplusone


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oneplusone.databinding.ActivityMainBinding
import com.example.oneplusone.fragment.HomeFragment
import com.example.oneplusone.fragment.MapFragment
import com.example.oneplusone.model.ProductData
import com.example.oneplusone.model.ProductDataViewModel
import com.example.oneplusone.model.ProductItemRecyclerAdapter
import com.example.oneplusone.util.ItemSpacingController

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var homeFragment:HomeFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.home_menu, HomeFragment()).commit()

        binding.navView.setOnItemSelectedListener{it->
            when(it.itemId) {
            }
        }

    }


}