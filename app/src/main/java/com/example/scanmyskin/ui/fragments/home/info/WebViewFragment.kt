package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.scanmyskin.databinding.FragmentWebViewBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import androidx.navigation.fragment.navArgs

class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    private val webViewFragmentArgs: WebViewFragmentArgs by navArgs()

    override fun setupUi(){
        with(binding){
            showProgressDialog()
            webView.visibility = View.VISIBLE
            webView.loadUrl(webViewFragmentArgs.url)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    dismissProgressDialog()
                }
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWebViewBinding
        get() = FragmentWebViewBinding::inflate
}