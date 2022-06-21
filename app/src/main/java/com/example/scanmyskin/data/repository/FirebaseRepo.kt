package com.example.scanmyskin.data.repository

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.helpers.ImageClassifier
import com.example.scanmyskin.helpers.isPasswordValid
import com.example.scanmyskin.helpers.makeToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class FirebaseRepo(private val applicationContext: Context, private val auth: FirebaseAuth, private val database: FirebaseFirestore, private val storage: FirebaseStorage, private val remoteModel: CustomRemoteModel) {
    private val TAG = "FirebaseRepo"

    private lateinit var imageClassifier: ImageClassifier

    init {
        RemoteModelManager.getInstance().isModelDownloaded(remoteModel)
            .addOnSuccessListener {
                if(it){
                    Log.d(TAG, "model available")
                    val options = CustomImageLabelerOptions.Builder(remoteModel).setConfidenceThreshold(0.0f).build()
                    imageClassifier = ImageClassifier(ImageLabeling.getClient(options))
                }
            }
    }

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

    suspend fun processImage(image: Bitmap, rotationDegrees: Int): Flow<List<ImageLabel>> = flow {
        imageClassifier.processImage(image, rotationDegrees).collect {
            emit(it)
        }
    }

    suspend fun processImage(image: Uri): Flow<List<ImageLabel>> = flow {
        imageClassifier.processImage(image).collect {
            emit(it)
        }
    }
}