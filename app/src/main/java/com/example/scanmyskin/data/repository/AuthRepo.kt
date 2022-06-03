package com.example.scanmyskin.data.repository

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.helpers.isPasswordValid
import com.example.scanmyskin.helpers.makeToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepo {
    private val TAG = "AuthRepo"
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance("gs://scanmyskin.appspot.com")

    suspend fun register(email: String, password: String): Boolean{
        var isRegistrationSuccessful = false
        try {
            auth.createUserWithEmailAndPassword(email,password).await()
            Log.d(TAG,ScanMySkin.context.getString(R.string.user_added_to_database))
            isRegistrationSuccessful = true
            makeToast(ScanMySkin.context.getString(R.string.registered_successfully))
        } catch (e: java.lang.Exception){
            Log.d(TAG ,e.message.toString())
            makeToast(e.message.toString())
        }
        return isRegistrationSuccessful
    }

    suspend fun signIn(email: String, password: String): Boolean{
        var signingSuccessful: Boolean = false
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            signingSuccessful = true
        } catch (e: Exception){
            Log.d(TAG,e.message.toString())
            makeToast(e.message.toString())
        }
        return signingSuccessful
    }

    suspend fun signOut(){
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!
    }

    suspend fun changePassword(email: String): Boolean{
        return try {
            auth.sendPasswordResetEmail(email).await()
            makeToast(ScanMySkin.context.getString(R.string.email_for_password_reset))
            true
        } catch (e: Exception){
            Log.d(TAG, e.message.toString())
            false
        }
    }

    suspend fun updatePassword(newPassword: String, confirmPassword: String): Boolean{
        return if(newPassword.isPasswordValid() && newPassword == confirmPassword){
            try {
                getCurrentUser().updatePassword(newPassword).await()
                makeToast(ScanMySkin.context.getString(R.string.password_changed))
                true
            } catch (e: Exception){
                Log.d(TAG, e.message.toString())
                makeToast(e.message.toString())
                false
            }
        } else {
            false
        }
    }

    suspend fun deleteAccount(): Boolean{
        getCurrentUser().delete().await()
        makeToast(Resources.getSystem().getString(R.string.account_deleted))
        return true
    }
}