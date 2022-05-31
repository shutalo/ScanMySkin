package com.example.scanmyskin.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentLoginBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun setupUi(){
        binding.navigate.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(LoginFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override val TAG: String
        get() = tag!!

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() =FragmentLoginBinding::inflate
}