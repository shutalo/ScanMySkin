package com.example.scanmyskin.ui.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.data.models.HistoryItem
import com.example.scanmyskin.data.repository.FirebaseRepo
import com.example.scanmyskin.helpers.SingleLiveEvent
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.mlkit.vision.label.ImageLabel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val repo: FirebaseRepo) : BaseViewModel() {

    private val TAG = "HomeViewModel"

    private val ORIENTATIONS = SparseIntArray()
    private var _isUserSignedOut: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var isUserSignedOut: LiveData<Boolean> = _isUserSignedOut
    private var _accountDeleted: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var accountDeleted: LiveData<Boolean> = _accountDeleted
    private var _diseasesRetrieved: MutableLiveData<List<Disease>> = MutableLiveData()
    var diseasesRetrieved: LiveData<List<Disease>> = _diseasesRetrieved
    private var _imageLabeled: MutableLiveData<ImageLabel> = MutableLiveData()
    var imageLabeled: LiveData<ImageLabel> = _imageLabeled
    private var _historyRetrieved: MutableLiveData<List<HistoryItem>> = MutableLiveData()
    var historyRetrieved: LiveData<List<HistoryItem>> = _historyRetrieved
    var imageUri: Uri? = null
    lateinit var imagePath: String
    lateinit var imageName: String
    var shouldRetrieveHistory: Boolean = true

    companion object {
        val REQUEST_TAKE_PHOTO = 0
        val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    fun takePhoto(activity: Activity) {
        viewModelScope.launch {
            val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(activity)
            bottomSheetDialog.setContentView(R.layout.dialog_take_photo)
            val takePhotoButton : ImageButton = bottomSheetDialog.findViewById(R.id.takePhoto)!!
            val chooseFromGallery : ImageButton = bottomSheetDialog.findViewById(R.id.gallery)!!

            takePhotoButton.setOnClickListener {
                bottomSheetDialog.dismiss()
                dispatchTakePictureIntent(activity)
            }
            chooseFromGallery.setOnClickListener {
                bottomSheetDialog.dismiss()
                dispatchChooseFromGalleryIntent(activity)
            }
            bottomSheetDialog.show()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = ScanMySkin.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            imagePath = absolutePath
            imageName = this.name
        }
    }

    private fun dispatchTakePictureIntent(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(ScanMySkin.context.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        ScanMySkin.context,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(activity, takePictureIntent, REQUEST_TAKE_PHOTO, null)
                }
            }
        }
    }

    private fun dispatchChooseFromGalleryIntent(activity: Activity) {
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
    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(cameraId: String, activity: Activity, context: Context): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation)

        // On most devices, the sensor orientation is 90 degrees, but for some
        // devices it is 270 degrees. For devices with a sensor orientation of
        // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360

        // Return the corresponding FirebaseVisionImageMetadata rotation value.
        val result: Int
        when (rotationCompensation) {
            0 -> result = FirebaseVisionImageMetadata.ROTATION_0
            90 -> result = FirebaseVisionImageMetadata.ROTATION_90
            180 -> result = FirebaseVisionImageMetadata.ROTATION_180
            270 -> result = FirebaseVisionImageMetadata.ROTATION_270
            else -> {
                result = FirebaseVisionImageMetadata.ROTATION_0
                Log.e(TAG, "Bad rotation value: $rotationCompensation")
            }
        }
        return result
    }

    fun processImage(){
        viewModelScope.launch {
            imageUri?.let { uri ->
                repo.processImage(uri).collect { labels ->
                    if(labels.isNotEmpty()){
                        for (label in labels) {
                            Log.d(TAG, label.text)
                            Log.d(TAG, label.confidence.toString())
                        }
                        labels.maxByOrNull { it.confidence }.apply {
                            this?.let {
                                _imageLabeled.postValue(it)
                                repo.saveResult(imageName, uri, it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            repo.signOut()
            _isUserSignedOut.postValue(true)
        }
    }

    fun deleteAccount(){
        viewModelScope.launch {
            shouldShowProgressDialog(true)
            repo.deleteAccount().collect {
                _accountDeleted.postValue(it)
            }
        }
    }

    fun retrieveDiseases(){
        viewModelScope.launch {
            shouldShowProgressDialog(true)
            _diseasesRetrieved.postValue(repo.retrieveDiseases())
        }
    }

    fun retrieveHistory(){
        viewModelScope.launch {
            shouldShowProgressDialog(true)
            repo.retrieveHistory().collect {
                _historyRetrieved.postValue(it)
            }
        }
    }
}