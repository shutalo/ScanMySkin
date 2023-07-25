package com.example.scanmyskin.helpers

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun camelCaseString(string: String): String {
    return string.substring(0, 1).uppercase(Locale.getDefault()) + string.substring(1)
        .lowercase(Locale.getDefault())
}

fun makeToast(message: String, lengthLong: Boolean = false) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(
            ScanMySkin.context,
            message,
            if (lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }
}

fun formatChance(chance: Float): String {
    val percentage = chance * 100
    return if (percentage.toString().length >= 5) {
        percentage.toString().subSequence(0, 5).toString()
    } else {
        percentage.toString()
    }
}

fun formatStringDisease(string: String): String {
    return string.replace("_", " ")
}

fun String.isPasswordValid(): Boolean {
    if (this.length < 8) {
        return false
    }
    val pattern: Pattern
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    val matcher: Matcher = pattern.matcher(this)

    return matcher.matches()
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun validateRegistrationInput(email: String, password: String, confirmedPassword: String): Boolean {
    if (email.isEmailValid()) {
        if (password.isPasswordValid()) {
            if (confirmedPassword == password) {
                return true
            } else {
                makeToast(ScanMySkin.context.getString(R.string.password_must_match))
            }
        } else {
            makeToast(ScanMySkin.context.getString(R.string.password_wrong_format))
        }
    } else {
        makeToast(ScanMySkin.context.getString(R.string.email_error))
    }
    return false
}

// maybe move this to baseActivity
fun initPopupMenu(anchor: View, context: Context, listener: PopupMenu.OnMenuItemClickListener) {
    val popup = PopupMenu(context, anchor)
    val inflater = popup.menuInflater
    inflater.inflate(R.menu.menu, popup.menu)
    popup.setOnMenuItemClickListener(listener)
    popup.show()
}