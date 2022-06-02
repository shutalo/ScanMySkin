package com.example.scanmyskin.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentLoginBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by sharedViewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.isSigningInSuccessful.observe(this){
            if(it){
                viewModel.shouldShowProgressDialog(false)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity())
            }
        }
        binding.login.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            showProgressDialog()
            viewModel.signIn(binding.email.text.toString(), binding.password.text.toString())
        }
        binding.forgotPasswordTv.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate
}