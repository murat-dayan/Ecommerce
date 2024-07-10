package com.muratdayan.ecommerce.presentation.auth.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.domain.model.User
import com.muratdayan.ecommerce.databinding.FragmentRegisterBinding
import com.muratdayan.ecommerce.util.RegisterValidation
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "registerFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.navigate_registerFragment_to_loginFragment)
        }

        binding.apply {
            btnRegisterRegister.setOnClickListener {
                val user = User(
                    etFirstNameRegister.text.toString().trim(),
                    etLastNameRegister.text.toString().trim(),
                    etEmailRegister.text.toString().trim()
                )
                val password = etPasswordRegister.text.toString()
                registerViewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launch {
            registerViewModel.register.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e(TAG, result.message.toString())
                        binding.btnRegisterRegister.revertAnimation()
                    }

                    is Resource.Loading -> {
                        binding.btnRegisterRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d("test", result.data.toString())
                        binding.btnRegisterRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            registerViewModel.validation.collect { result ->
                if (result.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmailRegister.apply {
                            requestFocus()
                            error = result.email.errorMessage
                        }
                    }
                }

                if (result.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etPasswordRegister.apply {
                            requestFocus()
                            error = result.password.errorMessage
                        }
                    }
                }
            }
        }
    }
}