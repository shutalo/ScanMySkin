package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scanmyskin.databinding.FragmentDiseaseInfoBinding
import com.example.scanmyskin.ui.adapters.DiseaseInfoRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DiseaseInfoFragment : BaseFragment<FragmentDiseaseInfoBinding>(), DiseaseInfoRecyclerViewAdapter.OnUrlClicked {

    private val diseaseInfoFragmentArgs: DiseaseInfoFragmentArgs by navArgs()
    private lateinit var diseaseInfoRecyclerViewAdapter: DiseaseInfoRecyclerViewAdapter
    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi(){
        val disease = diseaseInfoFragmentArgs.disease
        with(binding){
            titleTv.text = disease.title
            descriptionTv.text = disease.description
        }
        viewModel.urlsRetrieved.observe(this){
            diseaseInfoRecyclerViewAdapter = DiseaseInfoRecyclerViewAdapter(it, this)
            binding.diseaseUrlsRecyclerView.adapter = diseaseInfoRecyclerViewAdapter
            dismissProgressDialog()
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDiseaseInfoBinding
        get() = FragmentDiseaseInfoBinding::inflate

    override fun onUrlClicked(url: String) {
        findNavController().navigate(DiseaseInfoFragmentDirections.actionDiseaseInfoFragmentToWebViewFragment(url))
    }
}