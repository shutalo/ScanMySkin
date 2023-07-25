package com.example.scanmyskin.ui.fragments.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.bumptech.glide.Glide
import com.example.scanmyskin.R
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.FragmentInfoBinding
import com.example.scanmyskin.ui.adapters.AdapterItemDecoration
import com.example.scanmyskin.ui.adapters.DiseaseRecyclerViewAdapter
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InfoFragment : BaseFragment<FragmentInfoBinding>(),
    DiseaseRecyclerViewAdapter.OnDiseaseClicked {

    private lateinit var diseaseRecyclerViewAdapter: DiseaseRecyclerViewAdapter
    private val viewModel by sharedViewModel<HomeViewModel>()
    override fun setupUi() {
        viewModel.diseasesRetrieved.observe(this) {
            preloadImages(it)
            diseaseRecyclerViewAdapter = DiseaseRecyclerViewAdapter(it, this)
            binding.diseasesRecyclerView.adapter = diseaseRecyclerViewAdapter
            binding.diseasesRecyclerView.addItemDecoration(AdapterItemDecoration(requireContext()))
            val args = InfoFragmentArgs.fromBundle(requireArguments())
            val disease = args.diseaseTitle
            disease?.let { diseaseTitle ->
                val position = diseaseRecyclerViewAdapter.getPositionOfDisease(diseaseTitle)
                CoroutineScope(Dispatchers.Main).launch {
                    delay(DELAY)
                    val layoutManager = binding.diseasesRecyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val smoothScroller = object : LinearSmoothScroller(requireContext()) {
                            override fun calculateDtToFit(
                                viewStart: Int,
                                viewEnd: Int,
                                boxStart: Int,
                                boxEnd: Int,
                                snapPreference: Int
                            ): Int {
                                return boxStart - viewStart + resources.getDimensionPixelOffset(R.dimen.scroll_offset) // Adjust the offset here
                            }
                        }
                        smoothScroller.targetPosition = position
                        layoutManager.startSmoothScroll(smoothScroller)
                    }
                }
            }
        }
    }

    private fun preloadImages(diseases: List<Disease>) {
        diseases.forEach { disease ->
            disease.diseaseExamples.values.forEach { url ->
                Glide.with(this)
                    .load(url)
                    .preload()
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate

    override fun onDiseaseClicked(disease: Disease) {
        findNavController().navigate(
            InfoFragmentDirections.actionInfoFragmentToDiseaseInfoFragment(
                disease
            )
        )
    }

    companion object {
        private const val DELAY = 100L
    }
}