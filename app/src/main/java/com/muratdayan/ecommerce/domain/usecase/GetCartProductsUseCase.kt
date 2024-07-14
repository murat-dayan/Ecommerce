package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    operator fun invoke() = shoppingRepository.getCartProducts()

}