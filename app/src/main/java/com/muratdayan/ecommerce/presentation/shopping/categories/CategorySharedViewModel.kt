package com.muratdayan.ecommerce.presentation.shopping.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.ecommerce.domain.model.Category
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.domain.usecase.GetOfferProductsUseCase
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class CategorySharedViewModel (
    private val getOfferProductsUseCase: GetOfferProductsUseCase,
    private val category: Category
): ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }


    fun fetchOfferProducts(){
        getOfferProductsUseCase(category.category).onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _offerProducts.emit(Resource.Loading())
                }

                is Resource.Success -> {
                    _offerProducts.emit(resource)
                }

                is Resource.Error -> {
                    _offerProducts.emit(Resource.Error(resource.message.toString()))
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun fetchBestProducts(){
        getOfferProductsUseCase(category.category).onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _bestProducts.emit(Resource.Loading())
                }

                is Resource.Success -> {
                    _bestProducts.emit(resource)
                }

                is Resource.Error -> {
                    _bestProducts.emit(Resource.Error(resource.message.toString()))
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }



}