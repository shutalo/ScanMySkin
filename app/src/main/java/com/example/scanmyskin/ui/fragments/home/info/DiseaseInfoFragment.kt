package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scanmyskin.databinding.FragmentDiseaseInfoBinding
import com.example.scanmyskin.ui.adapters.DiseaseInfoRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment

class DiseaseInfoFragment : BaseFragment<FragmentDiseaseInfoBinding>(), DiseaseInfoRecyclerViewAdapter.OnUrlClicked {

    private val diseaseInfoFragmentArgs: DiseaseInfoFragmentArgs by navArgs()
    private lateinit var diseaseInfoRecyclerViewAdapter: DiseaseInfoRecyclerViewAdapter

    override fun setupUi(){
        val disease = diseaseInfoFragmentArgs.disease
        diseaseInfoRecyclerViewAdapter = DiseaseInfoRecyclerViewAdapter(diseaseInfoFragmentArgs.disease.urls, this)
        with(binding){
            binding.diseaseUrlsRecyclerView.adapter = diseaseInfoRecyclerViewAdapter
            titleTv.text = disease.title
            descriptionTv.text = disease.description
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDiseaseInfoBinding
        get() = FragmentDiseaseInfoBinding::inflate

    override fun onUrlClicked(url: String) {
        findNavController().navigate(DiseaseInfoFragmentDirections.actionDiseaseInfoFragmentToWebViewFragment(url))
    }
}