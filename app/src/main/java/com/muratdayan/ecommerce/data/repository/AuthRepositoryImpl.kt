package com.muratdayan.ecommerce.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.domain.repository.AuthRepository
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
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
        }catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error("Invalid credentials"))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(Resource.Error("Invalid user"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }


}