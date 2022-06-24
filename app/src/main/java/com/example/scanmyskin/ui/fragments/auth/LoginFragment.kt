package com.example.scanmyskin.ui.fragments.auth

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentLoginBinding
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by sharedViewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.isSigningInSuccessful.observe(this){
            viewModel.shouldShowProgressDialog(false)
            if(it){
                activity?.finish()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity())
            }
        }
        binding.login.setOnClickListener{
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
            viewModel.signIn(binding.email.text.toString(), binding.password.text.toString())
            }.playOn(it)
        }
        binding.forgotPasswordTv.setOnClickListener{
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
            }.playOn(it)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate
}