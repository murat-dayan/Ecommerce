package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class GetOfferProductsUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke(categoryName: String) = authRepository.fetchOfferProducts(categoryName)

}