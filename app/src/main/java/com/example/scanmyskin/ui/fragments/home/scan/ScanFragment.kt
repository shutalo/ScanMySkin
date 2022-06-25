package com.example.scanmyskin.ui.fragments.home.scan

import android.R.attr.path
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentScanBinding
import com.example.scanmyskin.helpers.formatChance
import com.example.scanmyskin.helpers.formatStringDisease
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File


class ScanFragment : BaseFragment<FragmentScanBinding>() {

    private val TAG = "ScanFragment"

    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi(){
        viewModel.imageUri?.let {
            updateImage(it)
        }
        viewModel.imageLabeled.observe(this){
            it?.let {
                if(it.confidence < 0.5){
                    binding.result.text = getString(R.string.model_determination).plus(" ").plus(getString(R.string.consult_doctor))
                } else {
                    binding.result.text = getString(R.string.there_is_chance, formatChance(it.confidence), formatStringDisease(it.text))
                }
            }
        }
        binding.scan.setOnClickListener{
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
                viewModel.takePhoto(requireActivity())
            }.playOn(it)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding
        get() = FragmentScanBinding::inflate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(resultCode != Activity.RESULT_CANCELED){
            if(resultCode == Activity.RESULT_OK){
                if(requestCode == HomeViewModel.REQUEST_TAKE_PHOTO){
                    viewModel.imageUri = Uri.fromFile(File(viewModel.imagePath)).also {
                        updateImage(it)
                    }
                } else if(requestCode == HomeViewModel.REQUEST_SELECT_IMAGE_IN_ALBUM){
                    viewModel.imageUri = data?.data.also {
                        viewModel.imageName = with(it.toString()){
                            this.substring(this.lastIndexOf("/") + 1)
                        }
                        updateImage(it)
                    }
                }
                viewModel.processImage()
                viewModel.shouldRetrieveHistory = true
            }
        }
    }

    private fun updateImage(uri: Uri?){
        uri?.let {
            Glide.with(binding.root)
                .load(it)
                .into(binding.imageContainer)
        }
    }
}