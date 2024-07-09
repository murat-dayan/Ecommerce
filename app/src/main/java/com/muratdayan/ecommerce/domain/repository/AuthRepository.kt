package com.muratdayan.ecommerce.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createAccountWithEmailAndPassword(user:User,password:String) : Flow<Resource<FirebaseUser>>

    fun signInWithEmailAndPassword(email:String,password:String) : Flow<Resource<FirebaseUser>>

}