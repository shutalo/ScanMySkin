package com.example.scanmyskin.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scanmyskin.databinding.FragmentSplashScreenBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {
    override val TAG: String
        get() = tag!!

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashScreenBinding
        get() = FragmentSplashScreenBinding::inflate

    override fun setupUi() {
        TODO("Not yet implemented")
    }
}