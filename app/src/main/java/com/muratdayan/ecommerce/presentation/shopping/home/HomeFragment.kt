package com.muratdayan.ecommerce.presentation.shopping.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentHomeBinding
import com.muratdayan.ecommerce.presentation.shopping.adapter.HomeViewpagerAdapter
import com.muratdayan.ecommerce.presentation.shopping.categories.AccessoryFragment
import com.muratdayan.ecommerce.presentation.shopping.categories.ChairFragment
import com.muratdayan.ecommerce.presentation.shopping.categories.CupboardFragment
import com.muratdayan.ecommerce.presentation.shopping.categories.FurnitureFragment
import com.muratdayan.ecommerce.presentation.shopping.categories.MainCategoryFragment
import com.muratdayan.ecommerce.presentation.shopping.categories.TableFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        val viewpager2Adapter = HomeViewpagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = viewpager2Adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}