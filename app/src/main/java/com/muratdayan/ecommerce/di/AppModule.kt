package com.muratdayan.ecommerce.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.muratdayan.ecommerce.core.Constants.INTRODUCTION_SP
import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.data.repository.AuthRepositoryImpl
import com.muratdayan.ecommerce.data.repository.ShoppingRepositoryImpl
import com.muratdayan.ecommerce.domain.repository.AuthRepository
import com.muratdayan.ecommerce.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseCommon(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirebaseCommon(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore, firebaseCommon: FirebaseCommon): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore, firebaseCommon)

    @Provides
    @Singleton
    fun provideShoppingRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore, firebaseCommon: FirebaseCommon) = ShoppingRepositoryImpl(firebaseAuth, firestore, firebaseCommon)

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository) = RegisterUseCase(authRepository)



    @Provides
    fun provideIntroductionSP(
        application: Application
    ) = application.getSharedPreferences(INTRODUCTION_SP, MODE_PRIVATE)



}