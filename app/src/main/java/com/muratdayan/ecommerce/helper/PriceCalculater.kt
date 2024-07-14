package com.muratdayan.ecommerce.helper

fun Float?.getProductPrice(price:Float): Float{
    if (this == null){
        return price
    }
    val remainingPricePercentage = 1f - this
    val priceAfterOffer = price * remainingPricePercentage

    return priceAfterOffer
}