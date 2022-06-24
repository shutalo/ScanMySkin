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
import com.google.firebase.firestore.SetOptions
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


class FirebaseRepo(private val auth: FirebaseAuth, private val database: FirebaseFirestore, private val storage: FirebaseStorage, private val remoteModel: CustomRemoteModel) {
    private val TAG = "FirebaseRepo"

    private var imageClassifier: ImageClassifier? = null

    fun setupImageClassifier(){
        RemoteModelManager.getInstance().isModelDownloaded(remoteModel)
            .addOnSuccessListener {
                if(it){
                    Log.d(TAG, "model available")
                    val options = CustomImageLabelerOptions.Builder(remoteModel).setConfidenceThreshold(0.0f).setMaxResultCount(20).build()
                    imageClassifier = ImageClassifier(ImageLabeling.getClient(options))
                } else {
                    Log.d(TAG, "model unavailable")
                }
            }
    }

    suspend fun register(email: String, password: String): Boolean{
        var isRegistrationSuccessful = false
        try {
            auth.createUserWithEmailAndPassword(email,password).await()
            setupDatabase()
            Log.d(TAG,ScanMySkin.context.getString(R.string.user_added_to_database))
            isRegistrationSuccessful = true
            makeToast(ScanMySkin.context.getString(R.string.registered_successfully))
        } catch (e: java.lang.Exception){
            Log.d(TAG ,e.message.toString())
            makeToast(e.message.toString())
        }
        return isRegistrationSuccessful
    }

    private fun setupDatabase(){
        try {
            val newResults = HashMap<String,HashMap<String,Float>>()
            newResults["results"] = hashMapOf()
            database.collection("results").document(getCurrentUser().uid).set(newResults)
        } catch (e: Exception){
            e.printStackTrace()
        }
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

    fun signOut(){
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

    suspend fun deleteAccount(): Flow<Boolean> = flow{
        try{
            storage.reference.child("images/${getCurrentUser().uid}").delete().await()
            database.collection("results").document(getCurrentUser().uid).delete().await()
            getCurrentUser().delete().await()
            makeToast(Resources.getSystem().getString(R.string.account_deleted))
            emit(true)
        } catch (e: Exception){
            e.printStackTrace()
            emit(false)
        }
    }

    suspend fun retrieveDiseases(): List<Disease>? {
        val snapshot = database.collection("diseases").get().await()
        val diseases: ArrayList<Disease> = ArrayList()
        snapshot.forEach {
            diseases.add(Disease(it.data["title"] as String,it.data["description"] as String,it.data["urls"] as HashMap<String, String>))
        }
        return diseases
    }

    suspend fun processImage(image: Uri): Flow<List<ImageLabel>> = flow {
        imageClassifier?.processImage(image)?.collect {
            emit(it)
        }
    }

    fun uploadImage(imageUri: Uri, filename: String){
        auth.currentUser?.let {
            val storageReference = storage.reference.child("images/${it.uid}}/$filename")
            storageReference.putFile(imageUri)
        }
    }

    suspend fun saveResult(imageName: String, label: ImageLabel){
        val snapshot = database.collection("results").document(getCurrentUser().uid).get().await()
        try {
            val newResults = (snapshot.data?.get("results") as HashMap<String,HashMap<String,Float>>)
            val result: HashMap<String, Float> = HashMap()
            result[label.text] = label.confidence
            newResults[imageName] = result
            database.collection("results").document(getCurrentUser().uid).update("results",newResults).addOnCompleteListener {
                Log.d(TAG,"results updated")
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}