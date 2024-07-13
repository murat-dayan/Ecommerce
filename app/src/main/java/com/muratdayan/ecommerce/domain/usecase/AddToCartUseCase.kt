package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val authRepository: AuthRepository
){

    fun addUpdateCartProduct(cartProduct: CartProduct) = authRepository.addUpdateCartProduct(cartProduct)

    fun addNewProduct(cartProduct: CartProduct) = authRepository.addNewProduct(cartProduct)

    fun increaseQuantity(documentId:String,cartProduct: CartProduct) = authRepository.increaseQuantity(documentId,cartProduct)

}