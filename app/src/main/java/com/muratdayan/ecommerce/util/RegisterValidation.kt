package com.muratdayan.ecommerce.util

sealed class RegisterValidation {
    object Success: RegisterValidation()
    data class Failed(val errorMessage: String): RegisterValidation()
}

data class RegisterFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation
)