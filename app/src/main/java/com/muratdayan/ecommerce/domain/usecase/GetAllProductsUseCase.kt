package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.AuthRepository
import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    operator fun invoke() = shoppingRepository.getAllProducts()

}