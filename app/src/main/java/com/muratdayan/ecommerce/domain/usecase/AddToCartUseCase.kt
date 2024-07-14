package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.repository.AuthRepository
import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
){

    fun addUpdateCartProduct(cartProduct: CartProduct) = shoppingRepository.addUpdateCartProduct(cartProduct)

    fun addNewProduct(cartProduct: CartProduct) = shoppingRepository.addNewProduct(cartProduct)

    fun increaseQuantity(documentId:String,cartProduct: CartProduct) = shoppingRepository.increaseQuantity(documentId,cartProduct)

}