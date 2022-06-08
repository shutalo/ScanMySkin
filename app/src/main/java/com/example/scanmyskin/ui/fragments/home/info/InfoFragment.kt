package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.scanmyskin.databinding.FragmentInfoBinding
import com.example.scanmyskin.ui.adapters.DiseaseRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import org.koin.android.ext.android.inject

class InfoFragment : BaseFragment<FragmentInfoBinding>() {

    private val diseaseRecyclerViewAdapter: DiseaseRecyclerViewAdapter by inject()

    override fun setupUi(){
        binding.diseasesRecyclerView.adapter = diseaseRecyclerViewAdapter
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate
}