package com.muratdayan.ecommerce.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.muratdayan.ecommerce.core.Constants.USER_COLLECTION
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
    private val registerUseCase: RegisterUseCase,
    private val dbFirestore: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

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
                            saveUserInfo(result.data.uid, user=user)
//
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

    private fun saveUserInfo(userUid: String, user: User) {
        dbFirestore.collection(USER_COLLECTION).document(userUid).set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {exception->
                _register.value = Resource.Error(exception.message.toString())
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