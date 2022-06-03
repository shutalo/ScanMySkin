package com.example.scanmyskin.ui.fragments.home.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentHistoryBinding
import com.example.scanmyskin.databinding.FragmentScanBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.fragments.home.info.InfoFragment

class ScanFragment : BaseFragment<FragmentScanBinding>() {

    override fun setupUi(){
        //        binding.navigate.setOnClickListener{
        //            YoYo.with(Techniques.Bounce).playOn(it)
        //            findNavController().navigate(HomeFragment)
        //        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding
        get() = FragmentScanBinding::inflate
}