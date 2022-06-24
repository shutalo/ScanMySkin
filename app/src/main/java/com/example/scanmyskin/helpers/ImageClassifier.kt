package com.example.scanmyskin.helpers

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.scanmyskin.ScanMySkin.Companion.context
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.nio.ByteBuffer

class ImageClassifier(private val labeler: ImageLabeler) {

    private val TAG = "ImageClassifier"

    private fun getInputImageFromUri(uri: Uri): InputImage{
        return InputImage.fromFilePath(context, uri)
    }

    suspend fun processImage(image: Uri): Flow<List<ImageLabel>> = flow{
        emit(labelImage(getInputImageFromUri(image)))
    }

    private suspend fun labelImage(image: InputImage): List<ImageLabel> {
        var results: List<ImageLabel> = ArrayList()
        labeler.process(image)
            .addOnFailureListener { e ->
                e.printStackTrace()
                makeToast("Labeling failed.")
            }
            .addOnSuccessListener { labels ->
                makeToast("Labeling successful.")
                for (label in labels) {
                    Log.d(TAG, label.text)
                    Log.d(TAG, label.confidence.toString())
                    Log.d(TAG, label.index.toString())
                }
                results = labels
            }.await()
        return results
    }
}