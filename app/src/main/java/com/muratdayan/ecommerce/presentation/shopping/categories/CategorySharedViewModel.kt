package com.muratdayan.ecommerce.presentation.shopping.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.domain.usecase.GetOfferProductsUseCase
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CategorySharedViewModel @Inject constructor(
    private val getOfferProductsUseCase: GetOfferProductsUseCase,
): ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()


    fun fetchOfferProducts(categoryName:String){
        getOfferProductsUseCase(categoryName).onEach { resource ->
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



}