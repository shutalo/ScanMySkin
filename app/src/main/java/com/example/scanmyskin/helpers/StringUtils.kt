package com.example.scanmyskin.helpers

import java.time.LocalDateTime.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun camelCaseString(string: String): String {
    return string.substring(0,1).uppercase(Locale.getDefault()) + string.substring(1).uppercase(Locale.getDefault())
}