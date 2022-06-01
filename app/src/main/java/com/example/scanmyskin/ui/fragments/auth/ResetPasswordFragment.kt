package com.example.scanmyskin.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentRegisterBinding
import com.example.scanmyskin.databinding.FragmentResetPasswordBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val viewModel by viewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.isPasswordChangeRequested.observe(this){
            if(it){
                findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToChoosePasswordFragment())
            }
        }
        binding.reset.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            viewModel.requestPasswordChange(binding.email.toString())
        }
    }

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResetPasswordBinding
        get() = FragmentResetPasswordBinding::inflate
}