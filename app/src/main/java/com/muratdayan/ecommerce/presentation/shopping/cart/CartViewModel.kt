package com.muratdayan.ecommerce.presentation.shopping.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.usecase.ChangeQuantityUseCase
import com.muratdayan.ecommerce.domain.usecase.DeleteProductFromCartUseCase
import com.muratdayan.ecommerce.domain.usecase.GetCartProductsUseCase
import com.muratdayan.ecommerce.helper.getProductPrice
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val changeQuantityUseCase: ChangeQuantityUseCase,
    private val deleteProductFromCartUseCase: DeleteProductFromCartUseCase
) : ViewModel() {

    private val _cartProducts =
        MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()


    val productsPrice = cartProducts.map { result ->
        when (result) {
            is Resource.Success -> {
                calculatePrice(result.data)
            }

            else -> null
        }
    }

    private fun calculatePrice(data: List<CartProduct>?): Float {
        return data!!.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }

    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        getCartProductsUseCase().onEach { result ->
            _cartProducts.value = result

        }.launchIn(viewModelScope)
    }


    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ) {
        changeQuantityUseCase(cartProduct, quantityChanging).onEach { result ->
            _cartProducts.emit(result)
            if (result is Resource.Success) {
                result.data?.let { data ->
                    if (quantityChanging == FirebaseCommon.QuantityChanging.DECREASE && cartProduct.quantity == 1) {
                        _deleteDialog.emit(cartProduct)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        deleteProductFromCartUseCase(cartProduct)
    }

}