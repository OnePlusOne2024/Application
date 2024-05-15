package com.example.oneplusone.util

class SearchTextCheck {

    fun searchTextCheck(searchText: String):Boolean{
        searchText.let{
            return it != ""
        }
    }
}