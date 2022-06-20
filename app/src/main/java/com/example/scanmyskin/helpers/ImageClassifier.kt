package com.example.scanmyskin.helpers

import android.graphics.Bitmap
import android.net.Uri
import com.example.scanmyskin.ScanMySkin.Companion.context
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import java.nio.ByteBuffer

class ImageClassifier(private val labeler: ImageLabeler) {

    private val TAG = "ImageClassifier"

    private fun getInputImageFromUri(uri: Uri): InputImage{
        return InputImage.fromFilePath(context, uri)
    }

    private fun getInputImageFromByteArray(byteBuffer: ByteBuffer, rotationDegrees: Int): InputImage{
        return InputImage.fromByteBuffer(
            byteBuffer,
            /* image width */ 480,
            /* image height */ 360,
            rotationDegrees,
            InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
        )
    }

    private fun getInputImageFromBitmap(bitmap: Bitmap, rotationDegrees: Int): InputImage{
        return InputImage.fromBitmap(bitmap, rotationDegrees)
    }

    fun processImage(image: Uri){
        labelImage(getInputImageFromUri(image))
    }

    fun processImage(image: Bitmap, rotationDegrees: Int){
        labelImage(getInputImageFromBitmap(image, rotationDegrees))
    }

    fun processImage(image: ByteBuffer, rotationDegrees: Int = 0){
        labelImage(getInputImageFromByteArray(image, rotationDegrees))
    }

    private fun labelImage(image: InputImage){
        labeler.process(image)
            .addOnSuccessListener { labels ->
                // Task completed successfully
                // ...
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    val index = label.index
                }
                makeToast("Labeling successful.")
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                makeToast("Labeling failed.")
            }
    }
}