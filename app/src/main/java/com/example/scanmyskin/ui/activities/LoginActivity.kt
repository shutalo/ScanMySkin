package com.example.scanmyskin.ui.activities

import android.view.LayoutInflater
import com.example.scanmyskin.databinding.ActivityLoginBinding
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, AuthViewModel>() {

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override val viewModel: AuthViewModel by viewModel()
}