package com.muratdayan.ecommerce.presentation.dialog.introduction

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.core.Constants.INTRODUCTION_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate : StateFlow<Int> = _navigate

    companion object{
        const val SHOPPING_ACTIVITY = 23
        //const val ACCOUNT_OPTIONS_FRAGMENT = R.id.navigate_introductionFragment_to_accountOptionsFragment
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }else if (isButtonClicked){
            viewModelScope.launch {
                _navigate.emit(R.id.navigate_introductionFragment_to_accountOptionsFragment)
            }
        }else{
            Unit
        }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY,true).apply()
    }
}