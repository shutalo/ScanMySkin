package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.scanmyskin.databinding.FragmentInfoBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class InfoFragment : BaseFragment<FragmentInfoBinding>() {

    override fun setupUi(){
//        binding.navigate.setOnClickListener{
//            YoYo.with(Techniques.Bounce).playOn(it)
//            findNavController().navigate(HomeFragment)
//        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate
}