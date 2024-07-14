package com.muratdayan.ecommerce.presentation.shopping.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.usecase.ChangeQuantityUseCase
import com.muratdayan.ecommerce.domain.usecase.GetCartProductsUseCase
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val changeQuantityUseCase: ChangeQuantityUseCase
): ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()



    init {
        getCartProducts()
    }

    private fun getCartProducts(){
        getCartProductsUseCase().onEach { result ->
            _cartProducts.value = result
            if (result is Resource.Success){
                //cartProductsDocuments = result.
            }
        }.launchIn(viewModelScope)
    }

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){
        changeQuantityUseCase(cartProduct, quantityChanging).onEach { result ->
            _cartProducts.emit(result)
        }.launchIn(viewModelScope)
    }




}