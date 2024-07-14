package com.muratdayan.ecommerce.presentation.shopping

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.integrity.internal.f
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.ActivityShoppingBinding
import com.muratdayan.ecommerce.presentation.shopping.cart.CartViewModel
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val navController = findNavController(R.id.host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest {result ->
                when(result){
                    is Resource.Success -> {
                        val count = result.data?.size ?: 0
                        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> Unit
                }
            }
        }

    }
}