package com.muratdayan.ecommerce.domain.model

data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedColor: Int?=null,
    val selectedSize: String?=null
){
    constructor(): this(Product(),0,null,null)
}
