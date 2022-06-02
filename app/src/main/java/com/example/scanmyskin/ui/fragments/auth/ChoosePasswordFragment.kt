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
                viewModel.shouldShowProgressDialog(false)
                findNavController().navigate(ChoosePasswordFragmentDirections.actionChoosePasswordFragmentToLoginFragment())
            }
        }
        binding.choose.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            showProgressDialog()
            viewModel.changePassword(binding.password.text.toString(), binding.repeatPassword.text.toString())
        }
    }

    companion object {
        fun newInstance() = ChoosePasswordFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChoosePasswordBinding
        get() = FragmentChoosePasswordBinding::inflate
}