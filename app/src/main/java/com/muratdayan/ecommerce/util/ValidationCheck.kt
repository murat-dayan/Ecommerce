package com.muratdayan.ecommerce.util

import android.util.Patterns

fun validateEMail(email: String): RegisterValidation{
    if (email.isEmpty()){
        return RegisterValidation.Failed("Email cannot be empty")
    }

    if (!Patterns.EMAIL_ADDRESS.equals(email)){
        return RegisterValidation.Failed("Wrong email format")
    }

    return  RegisterValidation.Success
}

fun validatePassword(password:String) : RegisterValidation{
    if (password.isEmpty()){
        return RegisterValidation.Failed("Password cannot be empty")
    }

    if (password.length <6){
        return RegisterValidation.Failed("Password must contain at least 6 characters")
    }

    return RegisterValidation.Success
}