package com.example.scanmyskin.ui.fragments.auth

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentLoginBinding
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
                val extras = ActivityNavigator.Extras.Builder()
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .build()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(), extras)
            }
        }
        binding.login.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            viewModel.signIn(binding.email.text.toString(), binding.password.text.toString())
        }
        binding.forgotPasswordTv.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate
}