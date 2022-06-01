package com.example.scanmyskin.helpers

import android.R.attr
import android.text.TextUtils
import android.widget.Toast
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import java.time.LocalDateTime.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun camelCaseString(string: String): String {
    return string.substring(0,1).uppercase(Locale.getDefault()) + string.substring(1).uppercase(Locale.getDefault())
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun makeToast(message: String, lengthLong: Boolean = false){
    Toast.makeText(ScanMySkin.context, message, if(lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun String.isPasswordValid(): Boolean{
    val pattern: Pattern
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    val matcher: Matcher = pattern.matcher(this)

    return matcher.matches()
}