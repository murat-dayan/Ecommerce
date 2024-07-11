package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getAllProducts()

}