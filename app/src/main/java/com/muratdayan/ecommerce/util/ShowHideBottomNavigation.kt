package com.muratdayan.ecommerce.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.presentation.shopping.ShoppingActivity

fun Fragment.hideBottomNavView(){
    val bottomNavView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottom_navigation
    )
    bottomNavView.visibility = View.GONE
}

fun Fragment.showBottomNavView(){
    val bottomNavView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottom_navigation
    )
    bottomNavView.visibility = View.VISIBLE
}