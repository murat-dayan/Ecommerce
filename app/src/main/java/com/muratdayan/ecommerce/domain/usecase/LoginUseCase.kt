package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) = authRepository.signInWithEmailAndPassword(email, password)
}