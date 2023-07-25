package com.example.scanmyskin.ui.fragments.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.scanmyskin.ui.activities.BaseActivity

abstract class BaseFragment<viewBinding : ViewBinding> : Fragment() {

    private lateinit var _binding: viewBinding
    val binding: viewBinding
        get() = _binding

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> viewBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(this.javaClass.simpleName, "onCreateView()")
        _binding = bindingInflater(layoutInflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(this.javaClass.simpleName, "onViewCreated()")
        setupUi()
    }

    abstract fun setupUi()

    override fun onResume() {
        super.onResume()
        Log.d(this.javaClass.simpleName, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(this.javaClass.simpleName, "onPause()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(this.javaClass.simpleName, "onDestroy()")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(this.javaClass.simpleName, "onAttach()")
    }

    fun showProgressDialog() {
        (activity as BaseActivity<*, *>).showProgressDialog()
    }

    fun dismissProgressDialog() {
        (activity as BaseActivity<*, *>).dismissProgressDialog()
    }
}