package com.example.scanmyskin.ui.fragments.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentRegisterBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.concurrent.schedule

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel by sharedViewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.isUserRegisteredSuccessfully.observe(this) {
            if (it) {
                viewModel.shouldShowProgressDialog(false)
                findNavController().navigate(RegisterFragmentDirections.actionRegistrationFragmentToHomeActivity())
            }
        }
        binding.register.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            viewModel.register(binding.email.text.toString(), binding.password.text.toString(), binding.repeatPassword.text.toString())
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegisterBinding
        get() = FragmentRegisterBinding::inflate
}