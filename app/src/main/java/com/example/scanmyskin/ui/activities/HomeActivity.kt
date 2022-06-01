package com.example.scanmyskin.ui.activities

import android.os.Bundle
import com.example.scanmyskin.TensorFlow.Classifier

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.ActivityHomeBinding
import com.example.scanmyskin.databinding.ActivityLoginBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class HomeActivity : BaseActivity<ActivityHomeBinding>(), EasyPermissions.PermissionCallbacks{

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    companion object {
        private const val TAG = "MainActivity"
        private const val INPUT_WIDTH = 300
        private const val INPUT_HEIGHT = 300
        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128f
        private const val INPUT_NAME = "Mul"
        private const val OUTPUT_NAME = "final_result"
        private const val MODEL_FILE = "file:///android_asset/model.pb"
        private const val LABEL_FILE = "file:///android_asset/labels.txt"
        private const val REQUEST_CODE_PERMISSIONS = 1
    }

//    private var classifier: Classifier? = null
//    private var initializeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askForPermissions()
//        initializeTensorClassifier()
//        binding.buttonScan.setOnClickListener {
//
//        }
    }

//    private fun onImageCaptured(it: CameraKitImage) {
//        val bitmap = Bitmap.createScaledBitmap(it.bitmap, INPUT_WIDTH, INPUT_HEIGHT, false)
//        showCapturedImage(bitmap)
//
//        classifier?.let {
//            try {
//                showRecognizedResult(it.recognizeImage(bitmap))
//            } catch (e: java.lang.RuntimeException) {
//                Log.e(TAG, "Crashing due to classification.closed() before the recognizer finishes!")
//            }
//        }
//    }

    private fun showRecognizedResult(results: MutableList<Classifier.Recognition>) {
        runOnUiThread {
            setVisibilityOnCaptured(true)
            if (results.isEmpty()) {
//                textResult.text = getString(R.string.result_no_hero_found)
            } else {
                val hero = results[0].title
                val confidence = results[0].confidence
//                textResult.text = when {
//                    confidence > 0.95 -> getString(R.string.result_confident_hero_found, hero)
//                    confidence > 0.85 -> getString(R.string.result_think_hero_found, hero)
//                    else -> getString(R.string.result_maybe_hero_found, hero)
//                }
            }
        }
    }

    private fun showCapturedImage(bitmap: Bitmap?) {
//        runOnUiThread {
//            imageCaptured.visibility = View.VISIBLE
//            imageCaptured.setImageBitmap(bitmap)
//        }
    }

    private fun setVisibilityOnCaptured(isDone: Boolean) {
//        buttonRecognize.isEnabled = isDone
//        if (isDone) {
//            imageCaptured.visibility = View.VISIBLE
//            textResult.visibility = View.VISIBLE
//            progressBar.visibility = View.GONE
//        } else {
//            imageCaptured.visibility = View.GONE
//            textResult.visibility = View.GONE
//            progressBar.visibility = View.VISIBLE
//        }
    }

//    private fun initializeTensorClassifier() {
//        initializeJob = CoroutineScope(Dispatchers.IO).launch {
//            try {
//                classifier = TensorFlowImageClassifier.create(
//                    assets, MODEL_FILE, LABEL_FILE, INPUT_WIDTH, INPUT_HEIGHT,
//                    IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME
//                )
//
////                runOnUiThread {
////                    buttonRecognize.isEnabled = true
////                }
//            } catch (e: Exception) {
//                throw RuntimeException("Error initializing TensorFlow!", e)
//            }
//        }
//    }
//
//    private fun clearTensorClassifier() {
//        initializeJob?.cancel()
//        classifier?.close()
//    }

    override fun onDestroy() {
        super.onDestroy()
//        clearTensorClassifier()
    }

    private fun askForPermissions(){

        if(EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.d(TAG,"permissions already granted!",)
            Toast.makeText(this,"permission granted already!", Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions for managing your account and progress", REQUEST_CODE_PERMISSIONS, android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG,"permissions granted!",)
        Toast.makeText(this,"permissions granted!", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            Log.d(TAG,"permissions denied!",)
            AppSettingsDialog.Builder(this).build().show()
            Toast.makeText(this,"permissions denied!", Toast.LENGTH_SHORT).show()
        }
    }
}