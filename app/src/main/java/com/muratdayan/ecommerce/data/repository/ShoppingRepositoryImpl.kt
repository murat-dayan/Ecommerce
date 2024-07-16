package com.muratdayan.ecommerce.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.model.Product
import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import com.muratdayan.ecommerce.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ShoppingRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseCommon: FirebaseCommon
) : ShoppingRepository {

    private val pagingInfo = PagingInfo()

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _cartProducts =
        MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()
    private var cartProductsDocuments = emptyList<DocumentSnapshot>()

    override fun fetchProductsByCategoryName(categoryName: String): Flow<Resource<List<Product>>> =
        flow {
            emit(Resource.Loading())
            try {
                val products = firestore.collection("Products")
                    .whereEqualTo("category", categoryName)
                    .get()
                    .await()

                emit(Resource.Success(products.toObjects(Product::class.java)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    override fun getAllProducts(): Flow<Resource<List<Product>>> = flow {
        if (!pagingInfo.isPagingEnd) {
            emit(Resource.Loading())

            try {
                val products = firestore.collection("Products")
                    .limit(pagingInfo.bestProductsPage * 10)
                    .get()
                    .await()
                    .toObjects(Product::class.java)
                pagingInfo.isPagingEnd = products == pagingInfo.oldBestProducts
                pagingInfo.oldBestProducts = products

                emit(Resource.Success(products))

                pagingInfo.bestProductsPage++
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun fetchOfferProducts(categoryName: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())

        try {
            val products = firestore.collection("Products")
                .whereEqualTo("category", categoryName)
                .whereNotEqualTo("offerPercentage", null)
                .get()
                .await()
                .toObjects(Product::class.java)

            emit(Resource.Success(products))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun addUpdateCartProduct(cartProduct: CartProduct): Flow<Resource<CartProduct>> =
        channelFlow {
            send(Resource.Loading())

            try {
                val documents =
                    suspendCancellableCoroutine<List<DocumentSnapshot>> { continuation ->
                        firestore.collection("user").document(firebaseAuth.uid!!).collection("cart")
                            .whereNotEqualTo("product.id", cartProduct.product.id).get()
                            .addOnSuccessListener {
                                continuation.resume(it.documents)
                            }
                            .addOnFailureListener {
                                continuation.resumeWithException(it)
                            }
                    }

                if (documents.isEmpty()) {
                    addNewProduct(cartProduct).collect { result ->
                        send(result)
                    }
                } else {
                    val product = documents.first().toObject(CartProduct::class.java)
                    if (product == cartProduct) {
                        val documentId = documents.first().id
                        increaseQuantity(documentId, cartProduct).collect { result ->
                            send(result)
                        }
                    } else {
                        addNewProduct(cartProduct).collect { result ->
                            send(result)
                        }
                    }
                }
            } catch (e: Exception) {
                send(Resource.Error(e.message.toString()))
            }
        }

    override fun addNewProduct(cartProduct: CartProduct): Flow<Resource<CartProduct>> =
        channelFlow {
            try {
                suspendCancellableCoroutine<Unit> { continuation ->
                    firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
                        scope.launch {
                            if (e == null) {
                                send(Resource.Success(addedProduct!!))
                                continuation.resume(Unit)
                            } else {
                                send(Resource.Error(e.message.toString()))
                                continuation.resume(Unit)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                send(Resource.Error(e.message.toString()))
            }
        }

    override fun increaseQuantity(
        documentId: String,
        cartProduct: CartProduct
    ): Flow<Resource<CartProduct>> = channelFlow {
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                firebaseCommon.increaseQuantity(documentId) { _, exception ->
                    scope.launch {
                        if (exception == null) {
                            send(Resource.Success(cartProduct))
                            continuation.resume(Unit)
                        } else {
                            send(Resource.Error(exception.message.toString()))
                            continuation.resume(Unit)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(Resource.Error(e.message.toString()))
        }
    }

    override fun getCartProducts(): Flow<Resource<List<CartProduct>>> = channelFlow {
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("user").document(firebaseAuth.uid!!).collection("cart")
                    .addSnapshotListener { value, error ->

                        if (error != null || value == null) {
                            scope.launch {
                                send(Resource.Error(error?.message.toString()))
                            }
                        }else{
                            cartProductsDocuments = value.documents
                            val cartProducts = value.toObjects(CartProduct::class.java)
                            scope.launch {
                                send(Resource.Success(cartProducts))
                                _cartProducts.value = Resource.Success(cartProducts)
                            }
                        }

                    }
            }
        } catch (e: Exception) {
            send(Resource.Error(e.message.toString()))
        }
    }

    override fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ): Flow<Resource<List<CartProduct>>> = channelFlow {
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                val index = cartProducts.value.data?.indexOf(cartProduct)
                if (index != null && index != -1) {
                    val documentId = cartProductsDocuments[index].id
                    when (quantityChanging) {
                        FirebaseCommon.QuantityChanging.INCREASE -> {
                            scope.launch { send(Resource.Loading()) }
                            firebaseCommon.increaseQuantity(documentId) { result, exception ->
                                if (exception != null) {
                                    scope.launch {
                                        send(Resource.Error(exception.message.toString()))
                                    }
                                }
                            }
                        }

                        FirebaseCommon.QuantityChanging.DECREASE -> {
                            if (cartProduct.quantity == 1) {
                                // Ürün miktarı 1 olduğunda doğrudan boş liste göndermek yerine
                                // Ürün miktarını azaltma işlemini doğrudan yapabilirsiniz
                                scope.launch {
                                    send(Resource.Success(emptyList()))
                                }
                                return@suspendCancellableCoroutine
                            } else {
                                scope.launch { send(Resource.Loading()) }
                                firebaseCommon.decreaseQuantity(documentId) { result, exception ->
                                    if (exception != null) {
                                        scope.launch {
                                            send(Resource.Error(exception.message.toString()))
                                        }
                                    } else {
                                        // Güncellenmiş listeyi gönderin
                                        scope.launch { send(Resource.Success(updatedCartProducts())) }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("ShoppingRepository", e.message.toString())
            send(Resource.Error(e.message.toString()))
        }
    }

    private fun updatedCartProducts(): List<CartProduct> {
        return cartProducts.value.data ?: emptyList()
    }

    override fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductsDocuments[index].id
            firestore.collection("user").document(firebaseAuth.uid!!).collection("cart")
                .document(documentId).delete()
        }
    }


    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBestProducts: List<Product> = emptyList(),
        var isPagingEnd: Boolean = false
    ) {


    }
}