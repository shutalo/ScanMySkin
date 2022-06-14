package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.FragmentInfoBinding
import com.example.scanmyskin.ui.adapters.DiseaseRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InfoFragment : BaseFragment<FragmentInfoBinding>(), DiseaseRecyclerViewAdapter.OnDiseaseClicked {

    private lateinit var diseaseRecyclerViewAdapter: DiseaseRecyclerViewAdapter
    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi(){
        viewModel.diseasesRetrieved.observe(this){
            dismissProgressDialog()
            diseaseRecyclerViewAdapter = DiseaseRecyclerViewAdapter(it,this)
            binding.diseasesRecyclerView.adapter = diseaseRecyclerViewAdapter
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate

    override fun onDiseaseClicked(disease: Disease) {
        findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToDiseaseInfoFragment(disease))
    }
}