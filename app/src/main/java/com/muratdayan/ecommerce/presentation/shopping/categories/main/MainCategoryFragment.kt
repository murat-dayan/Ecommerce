package com.muratdayan.ecommerce.presentation.shopping.categories.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentMainCategoryBinding
import com.muratdayan.ecommerce.presentation.shopping.adapter.BestDealsAdapter
import com.muratdayan.ecommerce.presentation.shopping.adapter.BestProductsAdapter
import com.muratdayan.ecommerce.presentation.shopping.adapter.SpecialProductsAdapter
import com.muratdayan.ecommerce.util.Resource
import com.muratdayan.ecommerce.util.showBottomNavView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private var _binding: FragmentMainCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter
    private val mainCategoryViewModel: MainCategoryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpecialProductsRv()
        setUpBestDealsProductsRv()
        setUpBestProductsRv()

        specialProductsAdapter.onClick = {product ->  
            val b = Bundle().apply { putParcelable("product",product) }
            findNavController().navigate(R.id.navigate_homeFragment_to_productDetailFragment, b)
        }

        bestDealsAdapter.onClick = {product ->
            val b = Bundle().apply { putParcelable("product",product) }
            findNavController().navigate(R.id.navigate_homeFragment_to_productDetailFragment, b)
        }

        bestProductsAdapter.onClick = {product ->
            val b = Bundle().apply { putParcelable("product",product) }
            findNavController().navigate(R.id.navigate_homeFragment_to_productDetailFragment, b)
        }


        lifecycleScope.launch {
            mainCategoryViewModel.bestDealsProducts.collect{
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            mainCategoryViewModel.specialProducts.collect{
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            mainCategoryViewModel.bestProducts.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.bestProductsProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressBar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.bestProductsProgressBar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        binding.nestedSVMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                mainCategoryViewModel.fetchBestProducts()
            }
        })

    }

    private fun setUpBestProductsRv() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProductsAdapter
        }
    }

    private fun setUpBestDealsProductsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setUpSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        showBottomNavView()
    }


}