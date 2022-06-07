package com.example.scanmyskin.ui.fragments.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentResetPasswordBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val viewModel by sharedViewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.isPasswordChangeRequested.observe(this){
            if(it){
//                findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToChoosePasswordFragment())
                activity?.onBackPressed()
            }
        }
        binding.reset.setOnClickListener{
            YoYo.with(Techniques.Bounce).playOn(it)
            viewModel.requestPasswordChange(binding.email.text.toString())
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResetPasswordBinding
        get() = FragmentResetPasswordBinding::inflate
}