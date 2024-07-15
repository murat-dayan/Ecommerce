package com.muratdayan.ecommerce.presentation.shopping.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentCartBinding
import com.muratdayan.ecommerce.presentation.shopping.adapter.CartProductAdapter
import com.muratdayan.ecommerce.util.Resource
import com.muratdayan.ecommerce.util.VerticalItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartAdapter by lazy { CartProductAdapter() }
    private val cartViewModel: CartViewModel by activityViewModels<CartViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCartRv()

        lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest {result ->
                when(result){
                    is Resource.Success -> {
                        binding.pbCart.visibility = View.INVISIBLE
                        if (result.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherViews()
                        }else{
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(result.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.pbCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.pbCart.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            btnCheckOut.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            btnCheckOut.visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun setUpCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration( ))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}