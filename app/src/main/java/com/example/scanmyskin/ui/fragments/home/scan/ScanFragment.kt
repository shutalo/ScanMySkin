package com.example.scanmyskin.ui.fragments.home.scan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentScanBinding
import com.example.scanmyskin.helpers.EventObserver
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.helpers.HomeEvent
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File


class ScanFragment : BaseFragment<FragmentScanBinding>() {

    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi() {
        viewModel.events.observe(viewLifecycleOwner,
            EventObserver { event ->
                return@EventObserver when (event) {
                    is HomeEvent.ShowResult -> {
                        event.disease?.let {
                            findNavController().navigate(
                                ScanFragmentDirections.actionScanFragmentToResultFragment(
                                    event.disease,
                                    event.confidence
                                )
                            )
                        }
                        Unit
                    }
                }
            }
        )
        binding.scan.setOnClickListener {
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd {
                viewModel.takePhoto(requireActivity())
            }.playOn(it)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding
        get() = FragmentScanBinding::inflate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == HomeViewModel.REQUEST_TAKE_PHOTO) {
                    viewModel.imageUri = Uri.fromFile(File(viewModel.imagePath))
                } else if (requestCode == HomeViewModel.REQUEST_SELECT_IMAGE_IN_ALBUM) {
                    viewModel.imageUri = data?.data.also {
                        viewModel.imageName = with(it.toString()) {
                            this.substring(this.lastIndexOf("/") + 1)
                        }
                    }
                }
                viewModel.processImage()
                viewModel.shouldRetrieveHistory = true
            }
        }
    }
}