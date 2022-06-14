package com.example.scanmyskin.ui.fragments.home.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentScanBinding
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScanFragment : BaseFragment<FragmentScanBinding>() {

    private val TAG = "ScanFragment"

    private val viewModel by sharedViewModel<HomeViewModel>()

    override fun setupUi(){
        binding.scan.setOnClickListener{
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
                viewModel.takePhoto(requireActivity())
            }.playOn(it)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanBinding
        get() = FragmentScanBinding::inflate

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_CANCELED){
            var image: Bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.camera_placeholder)
            if(requestCode == HomeViewModel.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        image = data?.extras?.get("data") as Bitmap
                        viewModel.processImageFromBitmap(image)
                    } catch (e: Exception){
                        Log.d(TAG,e.message.toString())
                    }
                }

            } else if(requestCode == HomeViewModel.REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK){
                val imageUri = data?.data
                if (imageUri != null) {
                    image = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
                    viewModel.processImageFromUri(imageUri)
                }
            }
            binding.imageContainer.setImageBitmap(image)
        }
    }
}