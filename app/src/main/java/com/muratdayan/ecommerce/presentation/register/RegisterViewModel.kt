package com.muratdayan.ecommerce.presentation.register

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.data.model.User
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val register : Flow<Resource<FirebaseUser>> = _register

    fun createAccountWithEmailAndPassword(user: User,password:String){
        firebaseAuth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {result ->
                result.user?.let {firebaseUser->
                    _register.value = Resource.Success(firebaseUser)
                }

            }.addOnFailureListener {result->
                _register.value = Resource.Error(result.message.toString())

            }
    }
}