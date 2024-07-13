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
        addToCartUseCase.addUpdateCartProduct(cartProduct).onEach {result->
            when(result){
                is Resource.Loading -> {
                    _addToCart.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _addToCart.emit(Resource.Success(result.data!!))
                }
                is Resource.Error -> {
                    _addToCart.emit(Resource.Error(result.message.toString()))
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun addNewProduct(cartProduct: CartProduct){
        addToCartUseCase.addNewProduct(cartProduct).onEach {result->
            when(result){
                is Resource.Loading -> {
                    _addToCart.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _addToCart.emit(Resource.Success(result.data!!))
                }
                is Resource.Error -> {
                    _addToCart.emit(Resource.Error(result.message.toString()))
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun increaseQuantity(documentId:String,cartProduct: CartProduct){
        addToCartUseCase.increaseQuantity(documentId,cartProduct).onEach {result->
            when(result){
                is Resource.Loading -> {
                    _addToCart.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _addToCart.emit(Resource.Success(result.data!!))
                }
                is Resource.Error -> {
                    _addToCart.emit(Resource.Error(result.message.toString()))
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }


}