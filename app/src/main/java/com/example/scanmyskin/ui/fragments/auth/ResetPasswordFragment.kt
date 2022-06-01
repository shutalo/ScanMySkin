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

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    override fun setupUi(){
        binding.reset.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToChoosePasswordFragment())
        }
    }

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResetPasswordBinding
        get() = FragmentResetPasswordBinding::inflate
}