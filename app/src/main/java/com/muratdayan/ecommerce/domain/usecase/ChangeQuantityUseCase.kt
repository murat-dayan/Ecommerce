package com.muratdayan.ecommerce.domain.usecase

import com.muratdayan.ecommerce.core.FirebaseCommon
import com.muratdayan.ecommerce.domain.model.CartProduct
import com.muratdayan.ecommerce.domain.repository.ShoppingRepository
import javax.inject.Inject

class ChangeQuantityUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    operator fun invoke(cartProduct: CartProduct, quantityChanging: FirebaseCommon.QuantityChanging) =
        shoppingRepository.changeQuantity(cartProduct, quantityChanging)
}