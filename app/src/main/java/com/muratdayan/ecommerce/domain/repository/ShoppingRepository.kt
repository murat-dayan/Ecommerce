package com.muratdayan.ecommerce.domain.repository

import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun fetchProductsByCategoryName(categoryName: String): Flow<Resource<List<Product>>>

    fun getAllProducts(): Flow<Resource<List<Product>>>

    fun fetchOfferProducts(categoryName: String): Flow<Resource<List<Product>>>

    fun addUpdateCartProduct(cartProduct: CartProduct): Flow<Resource<CartProduct>>

    fun addNewProduct(cartProduct: CartProduct): Flow<Resource<CartProduct>>

    fun increaseQuantity(documentId: String, cartProduct: CartProduct): Flow<Resource<CartProduct>>

    fun getCartProducts(): Flow<Resource<List<CartProduct>>>

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ): Flow<Resource<List<CartProduct>>>

    fun deleteCartProduct(cartProduct: CartProduct)
}