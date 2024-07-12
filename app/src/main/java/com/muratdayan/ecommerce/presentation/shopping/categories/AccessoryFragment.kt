package com.muratdayan.ecommerce.presentation.shopping.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.muratdayan.ecommerce.domain.model.Category
import com.muratdayan.ecommerce.domain.usecase.GetOfferProductsUseCase
import com.muratdayan.ecommerce.util.BaseCategoryViewModelFactory
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccessoryFragment : BaseCategoryFragment() {

    @Inject
    lateinit var getOfferProductsUseCase: GetOfferProductsUseCase

    val categorySharedViewModel by viewModels<CategorySharedViewModel> {
        BaseCategoryViewModelFactory(getOfferProductsUseCase, Category.Accessory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            categorySharedViewModel.offerProducts.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showOfferProgressBar()
                    }
                    is Resource.Success -> {
                        offerAdapter.differ.submitList(resource.data)
                        hideOfferProgressBar()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), resource.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideOfferProgressBar()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            categorySharedViewModel.bestProducts.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showBestProductsProgressBar()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(resource.data)
                        hideBestProductsProgressBar()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), resource.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideBestProductsProgressBar()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onOfferPagingRequest() {
        super.onOfferPagingRequest()

    }

    override fun onBestProductsPagingRequest() {
        super.onBestProductsPagingRequest()

    }
}