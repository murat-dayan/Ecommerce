package com.muratdayan.ecommerce.presentation.shopping.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentBaseCategoryBinding
import com.muratdayan.ecommerce.presentation.shopping.adapter.BestProductsAdapter
import com.muratdayan.ecommerce.util.showBottomNavView

open class BaseCategoryFragment : Fragment() {

    private var _binding: FragmentBaseCategoryBinding? = null
    private val binding get() = _binding!!

    protected val offerAdapter : BestProductsAdapter by  lazy { BestProductsAdapter() }
    protected val bestProductsAdapter : BestProductsAdapter by  lazy { BestProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOfferRV()
        setUpBestProductsRV()

        bestProductsAdapter.onClick = {product ->
            val b = Bundle().apply { putParcelable("product",product) }
            findNavController().navigate(R.id.navigate_homeFragment_to_productDetailFragment, b)
        }

        offerAdapter.onClick = {product ->
            val b = Bundle().apply { putParcelable("product",product) }
            findNavController().navigate(R.id.navigate_homeFragment_to_productDetailFragment, b)
        }

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedSVBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                onBestProductsPagingRequest()
            }
        })

    }

    fun showOfferProgressBar(){
        binding.offerProductProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferProgressBar(){
        binding.offerProductProgressBar.visibility = View.GONE
    }

    fun showBestProductsProgressBar(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsProgressBar(){
        binding.bestProductsProgressBar.visibility = View.GONE
    }


    open fun onOfferPagingRequest(){

    }

    open fun onBestProductsPagingRequest(){

    }

    private fun setUpBestProductsRV() {
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = bestProductsAdapter
        }
    }

    private fun setUpOfferRV() {
        binding.rvOffer.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
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