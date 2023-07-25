package com.example.scanmyskin.ui.fragments.home.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.scanmyskin.databinding.FragmentHistoryBinding
import com.example.scanmyskin.ui.adapters.AdapterItemDecoration
import com.example.scanmyskin.ui.adapters.HistoryRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(),
    HistoryRecyclerViewAdapter.OnHistoryItemClickListener {

    private lateinit var historyRecyclerViewAdapter: HistoryRecyclerViewAdapter
    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi() {
        if (viewModel.shouldRetrieveHistory) {
            viewModel.retrieveHistory()
            viewModel.shouldRetrieveHistory = false
        }
        viewModel.historyRetrieved.observe(this) {
            dismissProgressDialog()
            historyRecyclerViewAdapter = HistoryRecyclerViewAdapter(it, this)
            binding.historyRecyclerView.adapter = historyRecyclerViewAdapter
            binding.historyRecyclerView.addItemDecoration(AdapterItemDecoration(requireContext()))
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate

    override fun onHistoryItemClicked(chance: Float, disease: String, uri: String) {
        viewModel.getResultDisease(disease)?.let {
            findNavController().navigate(
                HistoryFragmentDirections.actionHistoryFragmentToResultFragment(
                    it,
                    chance,
                    uri
                )
            )
        }
    }

}