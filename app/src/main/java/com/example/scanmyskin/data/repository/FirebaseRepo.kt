package com.example.scanmyskin.data.repository

import android.app.Activity
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.content.res.Resources
import android.graphics.Bitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.SparseIntArray
import androidx.annotation.RequiresApi
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.helpers.isPasswordValid
import com.example.scanmyskin.helpers.makeToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.IOException


class FirebaseRepo(private val applicationContext: Context, private val auth: FirebaseAuth, private val database: FirebaseFirestore, private val storage: FirebaseStorage, private val options: FirebaseVisionCloudDetectorOptions) {
    private val TAG = "FirebaseRepo"

    suspend fun register(email: String, password: String): Boolean{
        var isRegistrationSuccessful = false
        try {
            auth.createUserWithEmailAndPassword(email,password).await()
            Log.d(TAG,ScanMySkin.context.getString(R.string.user_added_to_database))
            isRegistrationSuccessful = true
            makeToast(ScanMySkin.context.getString(R.string.registered_successfully))
        } catch (e: java.lang.Exception){
            Log.d(TAG ,e.message.toString())
            makeToast(e.message.toString())
        }
        return isRegistrationSuccessful
    }

    suspend fun signIn(email: String, password: String): Boolean{
        var signingSuccessful: Boolean = false
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            signingSuccessful = true
        } catch (e: Exception){
            Log.d(TAG,e.message.toString())
            makeToast(e.message.toString())
        }
        return signingSuccessful
    }

    suspend fun signOut(){
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!
    }

    suspend fun changePassword(email: String): Boolean{
        return try {
            auth.sendPasswordResetEmail(email).await()
            makeToast(ScanMySkin.context.getString(R.string.email_for_password_reset))
            true
        } catch (e: Exception){
            Log.d(TAG, e.message.toString())
            false
        }
    }

    suspend fun updatePassword(newPassword: String, confirmPassword: String): Boolean{
        return if(newPassword.isPasswordValid() && newPassword == confirmPassword){
            try {
                getCurrentUser().updatePassword(newPassword).await()
                makeToast(ScanMySkin.context.getString(R.string.password_changed))
                true
            } catch (e: Exception){
                Log.d(TAG, e.message.toString())
                makeToast(e.message.toString())
                false
            }
        } else {
            false
        }
    }

    fun checkIfUserIsSignedIn() : Boolean {
        val currentUser : FirebaseUser? = auth.currentUser
        return currentUser != null
    }

    suspend fun deleteAccount(): Boolean{
        getCurrentUser().delete().await()
        makeToast(Resources.getSystem().getString(R.string.account_deleted))
        return true
    }

    suspend fun retrieveDiseases(): List<Disease>? {
        val snapshot = database.collection("diseases").get().await()
        val diseases: ArrayList<Disease> = ArrayList()
        snapshot.forEach {
            diseases.add(Disease(it.data["title"] as String,it.data["description"] as String,it.data["urls"] as HashMap<String, String>))
        }
        return diseases
    }

//    suspend fun processImageFromBitmap(image: Bitmap){
//        val image = FirebaseVisionImage.fromBitmap(image)
//    }
//
//    suspend fun processImageFromUri(uri: Uri){
//        val image: FirebaseVisionImage
//        try {
//            image = FirebaseVisionImage.fromFilePath(applicationContext, uri)
//            processImage(image)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private suspend fun processImage(image: FirebaseVisionImage){
//        val detector = FirebaseVision.getInstance().cloudImageLabeler
//        val result = detector.processImage(image).await()
//        result.forEach {
//            Log.d(TAG,it.toString())
//        }
//    }
}