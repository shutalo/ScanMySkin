package com.example.scanmyskin.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.ActivityLoginBinding
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val authViewModel by viewModel<AuthViewModel>()

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate
}