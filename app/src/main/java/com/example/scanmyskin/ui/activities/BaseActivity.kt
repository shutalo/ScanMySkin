package com.example.scanmyskin.ui.activities;

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.example.scanmyskin.R

abstract class BaseActivity<viewBinding: ViewBinding> : AppCompatActivity() {

    private lateinit var _binding: viewBinding
    val binding: viewBinding
        get() = _binding

    abstract val bindingInflater: (LayoutInflater) -> viewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)
        this.supportActionBar?.hide()
        Log.d(this.javaClass.simpleName,"onCreate()")
        setContentView(binding.root)
    }
}
