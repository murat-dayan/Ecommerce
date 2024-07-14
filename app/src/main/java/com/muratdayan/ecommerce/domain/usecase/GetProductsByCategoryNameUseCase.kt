package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.AuthRepository
import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetProductsByCategoryNameUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {

    operator fun invoke(categoryName: String) = shoppingRepository.fetchProductsByCategoryName(categoryName)

}