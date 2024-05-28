package com.example.oneplusone.activity


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.oneplusone.R

import com.example.oneplusone.databinding.ActivityMainBinding
import com.example.oneplusone.fragment.FavoriteFragment
import com.example.oneplusone.fragment.HomeFragment
import com.example.oneplusone.fragment.MapFragment
import com.example.oneplusone.viewModel.DataBaseViewModel
import com.example.oneplusone.viewModel.ProductDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter





@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val productDataViewModel: ProductDataViewModel by viewModels()
    private val dbViewModel: DataBaseViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var lastConnectTime: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        //한번만 실행되어야 함 어플을 다시 킬때마다 실행
        observeProductDataViewModel()
        //딱 한번만 실행되도록 하기 위해
//        productDataViewModel.updateCheckResult(loadConnectTime(this@MainActivity))
        productDataViewModel.getProductDataFromServer(loadConnectTime(this@MainActivity))

        setBottomNavigation()

    }

    private fun loadConnectTime(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("LastConnectTime", Context.MODE_PRIVATE)
        val lastConnectTime = sharedPreferences.getString("lastConnectTime",null)
        val editor = sharedPreferences.edit()


        //접속했으면 현재 날짜를 등록
        editor.putString("lastConnectTime", getCurrentDate().toString())
        editor.apply()


        Log.d("dateTime", lastConnectTime.toString())


        return lastConnectTime
        //서버와 통신해서 데이터를 가져와야함, 만약 null이라면 처음 접속한것이기 때문에 다가져와야함
        //서버쪽에서는 null이라면 true를 보내줄것임

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): LocalDateTime {

        return LocalDateTime.now()
    }

    private fun setBottomNavigation() {
//        binding.navView.selectedItemId = R.id.home_menu

        binding.navView.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.home_menu -> HomeFragment()
                R.id.map_menu -> MapFragment()
                R.id.favorite_menu -> FavoriteFragment()
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

    private fun observeProductDataViewModel() {
        productDataViewModel.serverProductDataList.observe(this) { serverProductDataList ->
            if (serverProductDataList != null && serverProductDataList.success) {

                dbViewModel.waitServerConnectProcess(false)
                dbViewModel.updateProductDatabase(serverProductDataList.result)

            }else{
                dbViewModel.waitServerConnectProcess(true)
            }
        }
    }

}