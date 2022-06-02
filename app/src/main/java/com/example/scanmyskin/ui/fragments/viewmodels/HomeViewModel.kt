package com.example.scanmyskin.ui.fragments.viewmodels

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    companion object {
        val REQUEST_TAKE_PHOTO = 0
        val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    fun takePhoto(activity: Activity) {
        viewModelScope.launch {
            val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.dialog_take_photo)
            val takePhotoButton : ImageButton = bottomSheetDialog.findViewById(R.id.takePhoto)!!
            val chooseFromGallery : ImageButton = bottomSheetDialog.findViewById(R.id.gallery)!!

            takePhotoButton.setOnClickListener {
                bottomSheetDialog.dismiss()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(ScanMySkin.context.packageManager) != null) {
                    ActivityCompat.startActivityForResult(
                        activity,
                        intent,
                        REQUEST_TAKE_PHOTO,
                        null
                    )
                } else {
//                    Log.d(MainActivity.TAG,"Photo could not be taken!")
                }
            }
            chooseFromGallery.setOnClickListener {
                bottomSheetDialog.dismiss()
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                if (intent.resolveActivity(ScanMySkin.context.packageManager) != null) {
                    ActivityCompat.startActivityForResult(
                        activity,
                        intent,
                        REQUEST_SELECT_IMAGE_IN_ALBUM,
                        null
                    )
                }
            }
            bottomSheetDialog.show()
        }
    }
}