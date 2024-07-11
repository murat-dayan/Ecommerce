package com.muratdayan.ecommerce.presentation.shopping.categories.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.domain.usecase.GetAllProductsUseCase
import com.muratdayan.ecommerce.domain.usecase.GetProductsByCategoryNameUseCase
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getProductsByCategoryNameUseCase: GetProductsByCategoryNameUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel(){

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts: StateFlow<Resource<List<Product>>> = _bestDealsProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    init {
        fetchSpecialProducts()
        fetchBestDealProducts()
        fetchBestProducts()
    }


    private fun fetchSpecialProducts(){

        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        getProductsByCategoryNameUseCase("Special Products").onEach { resource ->
            when(resource){
                is Resource.Loading -> {
                    _specialProducts.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _specialProducts.emit(resource)
                }
                is Resource.Error -> {
                    _specialProducts.emit(Resource.Error(resource.message.toString()))
                }
                else -> Unit
            }

        }.launchIn(viewModelScope)
    }

    private fun fetchBestDealProducts(){

        viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Loading())
        }
        getProductsByCategoryNameUseCase("Best Deals").onEach { resource ->
            when(resource){
                is Resource.Loading -> {
                    _bestDealsProducts.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _bestDealsProducts.emit(resource)
                }
                is Resource.Error -> {
                    _bestDealsProducts.emit(Resource.Error(resource.message.toString()))
                }
                else -> Unit
            }

        }.launchIn(viewModelScope)
    }


    private fun fetchBestProducts(){

        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        getAllProductsUseCase().onEach { resource ->
            when(resource){
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