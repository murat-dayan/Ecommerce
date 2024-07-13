package com.muratdayan.ecommerce.presentation.shopping.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentProductDetailBinding
import com.muratdayan.ecommerce.presentation.shopping.ShoppingActivity
import com.muratdayan.ecommerce.presentation.shopping.adapter.ColorsAdapter
import com.muratdayan.ecommerce.presentation.shopping.adapter.SizesAdapter
import com.muratdayan.ecommerce.presentation.shopping.adapter.ViewPagerImagesAdapter
import com.muratdayan.ecommerce.util.hideBottomNavView


class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewPagerImagesAdapter by lazy { ViewPagerImagesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }

    private val args by navArgs<ProductDetailFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        hideBottomNavView()
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setUpSizesRv()
        setUpColorsRv()
        setUpViewPager()

        binding.ivClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty()){
                tvProductColors.visibility = View.GONE
            }
            if (product.sizes.isNullOrEmpty()){
                tvProductSizes.visibility = View.GONE
            }
        }

        viewPagerImagesAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

    }

    private fun setUpViewPager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerImagesAdapter
        }
    }

    private fun setUpColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpSizesRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}