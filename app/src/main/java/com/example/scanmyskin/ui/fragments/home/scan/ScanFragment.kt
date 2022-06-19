package com.example.scanmyskin.ui.fragments.home.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
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
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == HomeViewModel.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        val image = data?.extras?.get("data") as Bitmap
                        val file = viewModel.createFile()
                        val out = FileOutputStream(file)
                        image.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()
                        Glide.with(binding.root)
                            .load(Uri.fromFile(file))
                            .into(binding.imageContainer)
                        viewModel.processImageFromUri(Uri.fromFile(file))
                    } catch (e: Exception){
                        Log.d(TAG,e.message.toString())
                    }
                }

            } else if(requestCode == HomeViewModel.REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK){
                val imageUri = data?.data
                imageUri?.let {
                    Glide.with(binding.root)
                        .load(it)
                        .into(binding.imageContainer)
                    viewModel.processImageFromUri(it)
                }
            }
        }
    }


}