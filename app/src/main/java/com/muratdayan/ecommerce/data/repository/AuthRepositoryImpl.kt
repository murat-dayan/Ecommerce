package com.muratdayan.ecommerce.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.domain.repository.AuthRepository
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseCommon: FirebaseCommon
) : AuthRepository {



    override fun createAccountWithEmailAndPassword(
        user: User,
        password: String
    ): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())

        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            result.user?.let { firebaseUser ->
                emit(Resource.Success(firebaseUser))
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Resource.Error("User already exists"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error("Invalid credentials"))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(Resource.Error("Invalid user"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                emit(Resource.Success(firebaseUser))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error("Invalid credentials"))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(Resource.Error("Invalid user"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun resetPassword(email: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success("Password reset email sent"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }


}

