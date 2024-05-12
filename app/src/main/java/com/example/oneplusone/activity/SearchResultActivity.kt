package com.example.oneplusone.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ActivitySearchBinding
import com.example.oneplusone.databinding.ActivitySearchResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultActivity : AppCompatActivity() {

    private val binding by lazy {ActivitySearchResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val searchText=intent.getStringExtra("searchText")
        if (searchText != null) {
            Log.d("searchText",searchText)
        }
    }
}