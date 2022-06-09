package com.example.scanmyskin.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentChoosePasswordBinding
import com.example.scanmyskin.databinding.FragmentStartingBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.concurrent.schedule

class StartingFragment : BaseFragment<FragmentStartingBinding>() {

    private val viewModel by sharedViewModel<AuthViewModel>()

    override fun setupUi(){
        viewModel.checkIfUserIsSignedIn()
        viewModel.isUserSignedIn.observe(this){
            Timer().schedule(500){
                activity?.runOnUiThread {
                    if(it){
                        activity?.finish()
                        val extras = ActivityNavigator.Extras.Builder()
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .build()
                        findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToHomeActivity(), extras)
                    } else {
                        with(binding){
                            authenticationFormLayout.visibility = View.VISIBLE
                            YoYo.with(Techniques.SlideInUp).playOn(authenticationFormLayout)
                            register.setOnClickListener{ button ->
                                YoYo.with(Techniques.Bounce).playOn(button)
                                findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToRegistrationFragment())
                            }
                            login.setOnClickListener{ button ->
                                YoYo.with(Techniques.Bounce).playOn(button)
                                findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToLoginFragment())
                            }
                        }
                    }
                }
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartingBinding
        get() = FragmentStartingBinding::inflate
}