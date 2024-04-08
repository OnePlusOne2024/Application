package com.example.oneplusone


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.oneplusone.databinding.ActivityMainBinding
import com.example.oneplusone.fragment.HomeFragment
import com.example.oneplusone.fragment.MapFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigation()

//        setFragment("home",homeFragment)
//        binding.navView.setOnItemSelectedListener{it->
//            when(it.itemId) {
//            }
//        }
    }
    private fun setBottomNavigation() {
        binding.navView.selectedItemId = R.id.home_menu
        replaceFragment(HomeFragment())

        binding.navView.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.home_menu -> HomeFragment()
                R.id.map_menu -> MapFragment()
                else -> HomeFragment()
            }
            replaceFragment(fragment)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment, "")
            .commit()
    }

}