package com.muratdayan.ecommerce.presentation.dialog.introduction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muratdayan.ecommerce.R
import com.muratdayan.ecommerce.databinding.FragmentIntroductionBinding
import com.muratdayan.ecommerce.presentation.shopping.ShoppingActivity
import com.muratdayan.ecommerce.presentation.dialog.introduction.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {

    private lateinit var binding: FragmentIntroductionBinding
    private val introductionViewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            introductionViewModel.navigate.collect{result->
                when(result){
                    SHOPPING_ACTIVITY->{
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    R.id.navigate_introductionFragment_to_accountOptionsFragment->{
                        findNavController().navigate(R.id.navigate_introductionFragment_to_accountOptionsFragment)
                    }
                    else-> Unit
                }
            }
        }

        binding.btnIntroductionStart.setOnClickListener {
            introductionViewModel.startButtonClick()
            findNavController().navigate(R.id.navigate_introductionFragment_to_accountOptionsFragment)
        }
    }


}