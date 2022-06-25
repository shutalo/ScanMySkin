package com.example.scanmyskin.ui.fragments.home.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentHistoryBinding
import com.example.scanmyskin.ui.adapters.DiseaseRecyclerViewAdapter
import com.example.scanmyskin.ui.adapters.HistoryRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.fragments.home.info.InfoFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(){

    private lateinit var historyRecyclerViewAdapter: HistoryRecyclerViewAdapter
    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi(){
        if(viewModel.shouldRetrieveHistory){
            viewModel.retrieveHistory()
            viewModel.shouldRetrieveHistory = false
        }
        viewModel.historyRetrieved.observe(this){
            dismissProgressDialog()
            historyRecyclerViewAdapter = HistoryRecyclerViewAdapter(it)
            binding.historyRecyclerView.adapter = historyRecyclerViewAdapter
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate
}