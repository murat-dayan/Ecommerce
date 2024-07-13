package com.muratdayan.ecommerce.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createAccountWithEmailAndPassword(user:User,password:String) : Flow<Resource<FirebaseUser>>

    fun signInWithEmailAndPassword(email:String,password:String) : Flow<Resource<FirebaseUser>>

    fun resetPassword(email: String) : Flow<Resource<String>>

    fun fetchProductsByCategoryName(categoryName:String) : Flow<Resource<List<Product>>>

    fun getAllProducts(): Flow<Resource<List<Product>>>

    fun fetchOfferProducts(categoryName: String): Flow<Resource<List<Product>>>

    fun addUpdateCartProduct(cartProduct: CartProduct): Flow<Resource<CartProduct>>

    fun addNewProduct(cartProduct: CartProduct) : Flow<Resource<CartProduct>>

    fun increaseQuantity(documentId:String,cartProduct: CartProduct) : Flow<Resource<CartProduct>>

}