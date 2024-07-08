package com.muratdayan.ecommerce.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.domain.usecase.RegisterUseCase
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register : Flow<Resource<FirebaseUser>> = _register

    fun createAccountWithEmailAndPassword(user: User, password:String){
        runBlocking {
            _register.emit(Resource.Loading())
        }
        registerUseCase(user, password).onEach { result ->
            when(result) {
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
    }
}