package com.example.scanmyskin.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scanmyskin.databinding.FragmentHomeBinding
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun setupUi(){
//        binding.navigate.setOnClickListener{
//            YoYo.with(Techniques.Bounce).playOn(it)
//            findNavController().navigate(HomeFragment)
//        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate
}