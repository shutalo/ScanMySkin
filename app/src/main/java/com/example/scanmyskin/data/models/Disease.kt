package com.example.scanmyskin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disease(
    val title: String,
    val advice: String,
    val warning: String,
    val description: String,
    val urls: HashMap<String, String>,
    val diseaseExamples: HashMap<String, String>
) : Parcelable