package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import javax.inject.Inject

class DeleteProductFromCartUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    operator fun invoke(cartProduct: CartProduct) =
        shoppingRepository.deleteCartProduct(cartProduct)

}