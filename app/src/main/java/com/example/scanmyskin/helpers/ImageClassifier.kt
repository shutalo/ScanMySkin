package com.example.scanmyskin.helpers

import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import kotlinx.coroutines.flow.Flow

interface ImageClassifier {
    fun getInputImageFromUri(uri: Uri): InputImage
    suspend fun processImage(image: Uri): Flow<List<ImageLabel>>
    suspend fun labelImage(image: InputImage): List<ImageLabel>
}