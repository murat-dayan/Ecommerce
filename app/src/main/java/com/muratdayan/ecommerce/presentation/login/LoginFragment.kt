package com.muratdayan.ecommerce.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentLoginBinding
import com.muratdayan.ecommerce.presentation.ShoppingActivity
import com.muratdayan.ecommerce.presentation.dialog.setUpBottomSheetDialog
import com.muratdayan.ecommerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.navigate_loginFragment_to_registerFragment)
        }

        binding.tvForgotPasswordLogin.setOnClickListener {
            setUpBottomSheetDialog {email:String->
                loginViewModel.resetPassword(email)
            }
        }

        lifecycleScope.launch {
            loginViewModel.resetPassword.collect{result->
                when (result) {
                    is Resource.Error -> {
                        Snackbar.make(requireView(),result.message.toString(),Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success ->{
                        Snackbar.make(requireView(),"Reset password link sent to your email",Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            btnLoginLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val password = etPasswordLogin.text.toString()
                loginViewModel.signInWithEmailAndPassword(email, password)
            }
        }

        lifecycleScope.launch {
            loginViewModel.login.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                        binding.btnLoginLogin.revertAnimation()
                    }
                    is Resource.Loading -> {
                        binding.btnLoginLogin.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.btnLoginLogin.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    else -> Unit
                }
            }

        }
    }
}