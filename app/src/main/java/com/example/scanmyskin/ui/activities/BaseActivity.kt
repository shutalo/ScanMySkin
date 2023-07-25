package com.example.scanmyskin.ui.activities;

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.scanmyskin.helpers.ProgressDialog
import com.example.scanmyskin.ui.viewmodels.BaseViewModel

abstract class BaseActivity<viewBinding : ViewBinding, baseViewModel : BaseViewModel> :
    AppCompatActivity() {

    private lateinit var _binding: viewBinding
    val binding: viewBinding
        get() = _binding

    abstract val bindingInflater: (LayoutInflater) -> viewBinding
    private var progressDialog: ProgressDialog? = null
    protected abstract val viewModel: baseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.javaClass.simpleName, "onCreate()")
        _binding = bindingInflater(layoutInflater)
        this.supportActionBar?.hide()
        viewModel.shouldShowProgressDialog.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        }
        setContentView(binding.root)
    }

    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}
