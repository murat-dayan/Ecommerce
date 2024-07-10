package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun signInWithEmailAndPassword(email: String, password: String) =
        authRepository.signInWithEmailAndPassword(email, password)

    fun resetPassword(email: String) = authRepository.resetPassword(email)
}