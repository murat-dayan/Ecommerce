package com.muratdayan.ecommerce.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muratdayan.ecommerce.domain.model.Category
import com.muratdayan.ecommerce.domain.usecase.GetOfferProductsUseCase
import com.muratdayan.ecommerce.presentation.shopping.categories.CategorySharedViewModel

class BaseCategoryViewModelFactory(
    private val getOfferProductsUseCase: GetOfferProductsUseCase,
    private val category: Category
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategorySharedViewModel(getOfferProductsUseCase,category) as T
    }


}