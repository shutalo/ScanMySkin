package com.example.scanmyskin.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentRegisterBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override fun setupUi(){
        binding.register.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(RegisterFragmentDirections.actionRegistrationFragmentToHomeActivity())
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegisterBinding
        get() = FragmentRegisterBinding::inflate
}