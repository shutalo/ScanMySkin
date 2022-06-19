package com.example.scanmyskin.ui.viewmodels

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.data.repository.FirebaseRepo
import com.example.scanmyskin.helpers.SingleLiveEvent
import com.example.scanmyskin.helpers.makeToast
import com.example.scanmyskin.ui.activities.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val repo: FirebaseRepo) : BaseViewModel() {

    private val TAG = "HomeViewModel"

    private var _isUserSignedOut: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var isUserSignedOut: LiveData<Boolean> = _isUserSignedOut
    private var _diseasesRetrieved: MutableLiveData<List<Disease>> = MutableLiveData()
    var diseasesRetrieved: LiveData<List<Disease>> = _diseasesRetrieved

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
                    startActivityForResult(
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
                    startActivityForResult(
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

    fun createFile(): File? {
        try {
            if(EasyPermissions.hasPermissions(ScanMySkin.context, android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                val timestamp = SimpleDateFormat.getDateTimeInstance().format(Date())
                val storageDir = ScanMySkin.context.getExternalFilesDir(null)
                val dir = File(storageDir?.absolutePath + "/images")
                if(!dir.exists()){
                    dir.mkdir()
                }
                val photo = File(dir,"$timestamp.jpg")
                if (!photo.createNewFile()) {
                    Log.d(TAG, "This file already exists: " + photo.absolutePath)
                }
                val imagePath = photo.absolutePath
                Log.d(TAG,imagePath)
                return photo
            }else {
                makeToast("No permission.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun processImageFromBitmap(image: Bitmap){
        viewModelScope.launch {
            repo.processImageFromBitmap(image)
        }
    }

    fun processImageFromUri(image: Uri){
        viewModelScope.launch {
            repo.processImageFromUri(image)
        }
    }

    fun signOut(){
        viewModelScope.launch {
            repo.signOut()
            _isUserSignedOut.postValue(true)
        }
    }

    fun retrieveDiseases(){
        viewModelScope.launch {
            shouldShowProgressDialog(true)
            _diseasesRetrieved.postValue(repo.retrieveDiseases())
        }
    }
}