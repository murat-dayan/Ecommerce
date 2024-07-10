package com.muratdayan.ecommerce.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.domain.usecase.LoginUseCase
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel(){

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch { _login.emit(Resource.Loading()) }
        loginUseCase.signInWithEmailAndPassword(email, password).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _login.emit(Resource.Error(result.message.toString()))
                }
                is Resource.Loading -> {
                    _login.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _login.emit(result)
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun resetPassword(email: String){
        viewModelScope.launch { _resetPassword.emit(Resource.Loading()) }

        loginUseCase.resetPassword(email).onEach { result->
            when(result){
                is Resource.Error -> {
                    _resetPassword.emit(result)
                }
                is Resource.Loading -> {
                    _resetPassword.emit(result)
                }
                is Resource.Success -> {
                    _resetPassword.emit(result)
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }






}