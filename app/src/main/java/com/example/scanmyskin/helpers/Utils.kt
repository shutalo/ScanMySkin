package com.example.scanmyskin.helpers

import android.R.attr
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun camelCaseString(string: String): String {
    return string.substring(0,1).uppercase(Locale.getDefault()) + string.substring(1).uppercase(Locale.getDefault())
}

fun makeToast(message: String, lengthLong: Boolean = false){
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(ScanMySkin.context, message, if(lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}

fun String.isPasswordValid(): Boolean{
    if(this.length < 8){
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

fun validateRegistrationInput(email: String, password: String, confirmedPassword: String): Boolean{
    if(email.isEmailValid()){
        if(password.isPasswordValid()){
            if(confirmedPassword == password){
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
//            if(email.isEmailValid()){
//                if(password.length > 8){
//                    if(password.isNotEmpty()){
//                        if(password.isPasswordValid()){
//                            if(password == repeatPassword){
//
//                            } else {
//                                makeToast(ScanMySkin.context.getString(R.string.password_must_match))
//                            }
//                        } else {
//                            makeToast(ScanMySkin.context.getString(R.string.password_wrong_format))
//                        }
//                    } else {
//                        makeToast(ScanMySkin.context.getString(R.string.password_must_not_be_empty))
//                    }
//                } else {
//                    makeToast(ScanMySkin.context.getString(R.string.password_short))
//                }
//            } else {
//                makeToast(ScanMySkin.context.getString(R.string.email_error))