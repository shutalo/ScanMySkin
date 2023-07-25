package com.example.scanmyskin.data.repository

import android.content.res.Resources
import android.net.Uri
import android.util.Log
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.data.models.HistoryItem
import com.example.scanmyskin.helpers.ImageClassifierImpl
import com.example.scanmyskin.helpers.isPasswordValid
import com.example.scanmyskin.helpers.makeToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class FirebaseRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val remoteModel: CustomRemoteModel
) : FirebaseRepo {
    private val TAG = "FirebaseRepo"

    private var imageClassifier: ImageClassifierImpl? = null

    override fun setupImageClassifier() {
        RemoteModelManager.getInstance().isModelDownloaded(remoteModel)
            .addOnSuccessListener {
                if (it) {
                    Log.d(TAG, "model available")
                    val options =
                        CustomImageLabelerOptions.Builder(remoteModel).setConfidenceThreshold(0.0f)
                            .setMaxResultCount(20).build()
                    imageClassifier = ImageClassifierImpl(ImageLabeling.getClient(options))
                } else {
                    Log.d(TAG, "model unavailable")
                    makeToast("model unavailable")
                }
            }
    }

    override suspend fun register(email: String, password: String): Boolean {
        var isRegistrationSuccessful = false
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            setupDatabase()
            Log.d(TAG, ScanMySkin.context.getString(R.string.user_added_to_database))
            isRegistrationSuccessful = true
        } catch (e: java.lang.Exception) {
            Log.d(TAG, e.message.toString())
            makeToast(e.message.toString())
        }
        return isRegistrationSuccessful
    }

    override fun setupDatabase() {
        try {
            val newResults = HashMap<String, HashMap<String, Float>>()
            newResults[KEY_RESULTS] = hashMapOf()
            database.collection(KEY_RESULTS).document(getCurrentUser().uid).set(newResults)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        var signingSuccessful: Boolean = false
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            signingSuccessful = true
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            makeToast(e.message.toString())
        }
        return signingSuccessful
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!
    }

    override suspend fun changePassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            makeToast(ScanMySkin.context.getString(R.string.email_for_password_reset))
            true
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            false
        }
    }

    override suspend fun updatePassword(newPassword: String, confirmPassword: String): Boolean {
        return if (newPassword.isPasswordValid() && newPassword == confirmPassword) {
            try {
                getCurrentUser().updatePassword(newPassword).await()
                makeToast(ScanMySkin.context.getString(R.string.password_changed))
                true
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                makeToast(e.message.toString())
                false
            }
        } else {
            false
        }
    }

    override fun checkIfUserIsSignedIn(): Boolean {
        val currentUser: FirebaseUser? = auth.currentUser
        return currentUser != null
    }

    override suspend fun deleteAccount(): Flow<Boolean> = flow {
        try {
            val snapshot =
                database.collection(KEY_RESULTS).document(getCurrentUser().uid).get().await()
            val results =
                (snapshot.data?.get(KEY_RESULTS) as HashMap<String, HashMap<String, String>>)
            results.forEach {
                storage.getReferenceFromUrl(it.value[KEY_URL]!!).delete().await()
            }
            database.collection(KEY_RESULTS).document(getCurrentUser().uid).delete().await()
            getCurrentUser().delete().await()
            makeToast(Resources.getSystem().getString(R.string.account_deleted))
            emit(true)
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            signOut()
            e.message?.substringAfterLast(SUBSTRING_TEXT_DELIMITER)?.let { makeToast(it) }
            emit(false)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(false)
        }
    }

    override suspend fun retrieveDiseases(): List<Disease> {
        val snapshot = database.collection(KEY_DISEASES).get().await()
        val diseases: ArrayList<Disease> = ArrayList()
        snapshot.forEach {
            diseases.add(
                Disease(
                    it.data[KEY_TITLE] as String,
                    it.data[KEY_ADVICE] as String,
                    it.data[KEY_WARNING] as String,
                    it.data[KEY_DESCRIPTION] as String,
                    it.data[KEY_URLS] as HashMap<String, String>,
                    it.data[KEY_DISEASE_EXAMPLES] as HashMap<String, String>
                )
            )
        }
        return diseases
    }

    override suspend fun processImage(image: Uri): Flow<List<ImageLabel>> = flow {
        imageClassifier?.processImage(image)?.collect {
            emit(it)
        } ?: makeToast("Error")
    }

    override suspend fun uploadImage(imageUri: Uri, filename: String): Flow<Uri> = flow {
        auth.currentUser?.let {
            val storageReference = storage.reference.child("$KEY_IMAGES/${it.uid}}/$filename")
            val url = storageReference.putFile(imageUri).await().storage.downloadUrl.await()
            emit(url)
        } ?: makeToast("Error")
    }

    override suspend fun saveResult(imageName: String, uri: Uri, label: ImageLabel) {
        uploadImage(uri, imageName).collect {
            val snapshot =
                database.collection(KEY_RESULTS).document(getCurrentUser().uid).get().await()
            try {
                val newResults =
                    (snapshot.data?.get(KEY_RESULTS) as HashMap<String, HashMap<String, String>>)
                val result: HashMap<String, String> = HashMap()
                result[label.text] = label.confidence.toString()
                result[KEY_URL] = it.toString()
                newResults[imageName] = result
                database.collection(KEY_RESULTS).document(getCurrentUser().uid)
                    .update(KEY_RESULTS, newResults).addOnCompleteListener {
                    Log.d(TAG, "results updated")
                }
            } catch (e: Exception) {
                makeToast("Error")
                e.printStackTrace()
            }
        }

    }

    override suspend fun retrieveHistory(): Flow<List<HistoryItem>> = flow {
        val snapshot = database.collection(KEY_RESULTS).document(getCurrentUser().uid).get().await()
        val results = (snapshot.data?.get(KEY_RESULTS) as HashMap<String, HashMap<String, String>>)
        val items = ArrayList<HistoryItem>()
        val imageKeys = ArrayList<String>()
        results.forEach {
            imageKeys.add(it.key)
        }
        imageKeys.forEach {
            results[it].apply {
                val diseaseKey = this?.keys.run {
                    lateinit var diseaseKey: String
                    this!!.forEach { key ->
                        if (key != KEY_URL) {
                            diseaseKey = key
                        }
                    }
                    diseaseKey
                }
                val chance = this?.get(diseaseKey)
                val url = this?.get(KEY_URL)
                items.add(HistoryItem(chance!!, diseaseKey, url!!))
            }
        }
        emit(items)
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_RESULTS = "results"
        private const val KEY_URL = "url"
        private const val KEY_URLS = "urls"
        private const val KEY_DISEASE_EXAMPLES = "disease_examples"
        private const val KEY_IMAGES = "images"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_ADVICE = "advice"
        private const val KEY_WARNING = "warning"
        private const val KEY_DISEASES = "diseases"
        private const val SUBSTRING_TEXT_DELIMITER = "authentication. "
    }
}