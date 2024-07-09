package com.muratdayan.ecommerce.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.domain.usecase.RegisterUseCase
import com.muratdayan.ecommerce.util.RegisterFieldState
import com.muratdayan.ecommerce.util.RegisterValidation
import com.muratdayan.ecommerce.util.Resource
import com.muratdayan.ecommerce.util.validateEMail
import com.muratdayan.ecommerce.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {

        if (checkValidation(user, password)){
            runBlocking {
                _register.emit(Resource.Loading())
            }
            registerUseCase(user, password).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _register.emit(Resource.Error(result.message.toString()))
                    }

                    is Resource.Loading -> {
                        _register.emit(Resource.Loading())
                    }

                    is Resource.Success -> {
                        result.data?.let {
                            _register.emit(Resource.Success(result.data))
                        }
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)
        }else{
            val registerFieldState = RegisterFieldState(
                validateEMail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun checkValidation(user: User, password: String) :Boolean {
        val emailValidation = validateEMail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
}