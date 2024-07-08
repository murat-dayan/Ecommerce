package com.muratdayan.ecommerce.presentation.register

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.muratdayan.ecommerce.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _register = MutableStateFlow<>()

    fun createAccountWithEmailAndPassword(user: User,password:String){
        firebaseAuth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}