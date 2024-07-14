package com.muratdayan.ecommerce.presentation.shopping.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.usecase.AddToCartUseCase
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase
): ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()


    fun addUpdateProductCart(cartProduct: CartProduct){
        addToCartUseCase.addUpdateCartProduct(cartProduct).onEach {
            _addToCart.emit(it)
        }.launchIn(viewModelScope)
    }

    fun addNewProduct(cartProduct: CartProduct){
        addToCartUseCase.addNewProduct(cartProduct).onEach {
            _addToCart.emit(it)
        }.launchIn(viewModelScope)
    }

    fun increaseQuantity(documentId:String,cartProduct: CartProduct){
        addToCartUseCase.increaseQuantity(documentId,cartProduct).onEach {
            _addToCart.emit(it)
        }.launchIn(viewModelScope)
    }


}