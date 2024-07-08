package com.muratdayan.ecommerce.di

import com.google.firebase.auth.FirebaseAuth
import com.muratdayan.ecommerce.data.repository.AuthRepositoryImpl
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
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository) = RegisterUseCase(authRepository)

}