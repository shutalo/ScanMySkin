package com.example.scanmyskin.data.repository

import android.net.Uri
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.data.models.HistoryItem
import com.google.firebase.auth.FirebaseUser
import com.google.mlkit.vision.label.ImageLabel
import kotlinx.coroutines.flow.Flow

interface FirebaseRepo {
    fun setupImageClassifier()
    suspend fun register(email: String, password: String): Boolean
    suspend fun signIn(email: String, password: String): Boolean
    fun signOut()
    fun getCurrentUser(): FirebaseUser
    suspend fun changePassword(email: String): Boolean
    suspend fun updatePassword(newPassword: String, confirmPassword: String): Boolean
    fun checkIfUserIsSignedIn(): Boolean
    suspend fun deleteAccount(): Flow<Boolean>
    suspend fun retrieveDiseases(): List<Disease>?
    suspend fun processImage(image: Uri): Flow<List<ImageLabel>>
    suspend fun uploadImage(imageUri: Uri, filename: String): Flow<Uri>
    suspend fun saveResult(imageName: String, uri: Uri, label: ImageLabel)
    suspend fun retrieveHistory(): Flow<List<HistoryItem>>
    fun setupDatabase()
}