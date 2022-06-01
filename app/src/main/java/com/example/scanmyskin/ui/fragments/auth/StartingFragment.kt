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
import com.example.scanmyskin.databinding.FragmentChoosePasswordBinding
import com.example.scanmyskin.databinding.FragmentStartingBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class StartingFragment : BaseFragment<FragmentStartingBinding>() {
    override fun setupUi(){
        binding.register.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToRegistrationFragment())
        }
        binding.login.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToLoginFragment())
        }
    }

    companion object {
        fun newInstance() = ChoosePasswordFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartingBinding
        get() = FragmentStartingBinding::inflate
}