package com.example.scanmyskin.ui.activities;

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewbinding.ViewBinding
import com.example.scanmyskin.R
import com.example.scanmyskin.helpers.ProgressDialog
import com.example.scanmyskin.ui.fragments.viewmodels.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseActivity<viewBinding: ViewBinding, baseViewModel: BaseViewModel> : AppCompatActivity() {

    private lateinit var _binding: viewBinding
    val binding: viewBinding
        get() = _binding

    abstract val bindingInflater: (LayoutInflater) -> viewBinding
    private var progressDialog: ProgressDialog? = null
    protected abstract val viewModel: baseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.javaClass.simpleName,"onCreate()")
        _binding = bindingInflater(layoutInflater)
        this.supportActionBar?.hide()
        viewModel.shouldShowProgressDialog.observe(this){
            if(it){
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        }
        setContentView(binding.root)
    }

    fun showProgressDialog(title: String? = null){
        if(progressDialog == null){
            progressDialog = ProgressDialog(this)
        }
        progressDialog!!.show(title)
    }

    fun dismissProgressDialog(){
        progressDialog?.dismiss()
    }
}
