package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.FragmentInfoBinding
import com.example.scanmyskin.ui.adapters.DiseaseRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import org.koin.android.ext.android.inject

class InfoFragment : BaseFragment<FragmentInfoBinding>(), DiseaseRecyclerViewAdapter.OnDiseaseClicked {

    private var diseaseRecyclerViewAdapter: DiseaseRecyclerViewAdapter
    private val diseases: List<Disease> by inject()

    init {
        diseaseRecyclerViewAdapter = DiseaseRecyclerViewAdapter(diseases,this)
    }

    override fun setupUi(){
        binding.diseasesRecyclerView.adapter = diseaseRecyclerViewAdapter
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate

    override fun onDiseaseClicked(disease: Disease) {
        findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToDiseaseInfoFragment(disease))
    }
}