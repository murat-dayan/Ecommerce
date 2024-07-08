package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator  fun invoke(user: User,password:String) = authRepository.createAccountWithEmailAndPassword(user,password)
}