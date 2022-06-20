package com.example.scanmyskin.ui.fragments.home.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentScanBinding
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.FileOutputStream


class ScanFragment : BaseFragment<FragmentScanBinding>() {

    private val TAG = "ScanFragment"

    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi(){
        viewModel.imageUri?.let {
            updateImage(it)
        }
        binding.scan.setOnClickListener{
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
                viewModel.takePhoto(requireActivity())
            }.playOn(it)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding
        get() = FragmentScanBinding::inflate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == HomeViewModel.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        withContext(Dispatchers.Main) {
                            viewModel.imageUri = Uri.fromFile(viewModel.imagePath?.let { File(it) })
                            updateImage(Uri.fromFile(viewModel.imagePath?.let { File(it) }))
                        }
                        viewModel.imageUri?.let { viewModel.processImage(it) }
                    } catch (e: Exception){
                        Log.d(TAG,e.message.toString())
                    }
                }

            } else if(requestCode == HomeViewModel.REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK){
                val imageUri = data?.data
                imageUri?.let {
                    viewModel.imageUri = it
                    updateImage(it)
                    viewModel.processImage(it)
                }
            }
        }
    }

    private fun updateImage(uri: Uri){
        try {
            Glide.with(binding.root)
                .load(uri)
                .into(binding.imageContainer)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}