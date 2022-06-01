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
import com.example.scanmyskin.databinding.FragmentRegisterBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoosePasswordFragment : BaseFragment<FragmentChoosePasswordBinding>() {

    private val viewModel by viewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.isPasswordChangedSuccessfully.observe(this){
            if(it){
                findNavController().navigate(ChoosePasswordFragmentDirections.actionChoosePasswordFragmentToLoginFragment())
            }
        }
        binding.choose.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            viewModel.changePassword(binding.password.toString(), binding.repeatPassword.toString())
        }
    }

    companion object {
        fun newInstance() = ChoosePasswordFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChoosePasswordBinding
        get() = FragmentChoosePasswordBinding::inflate
}