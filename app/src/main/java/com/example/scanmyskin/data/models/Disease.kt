package com.example.scanmyskin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disease(val title: String, val description: String, val urls: HashMap<String, String>) : Parcelable