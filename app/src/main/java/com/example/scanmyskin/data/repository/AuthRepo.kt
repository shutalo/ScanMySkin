package com.example.scanmyskin.data.repository

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
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
            Log.d(TAG,Resources.getSystem().getString(R.string.user_added_to_database))
            isRegistrationSuccessful = true
            makeToast(Resources.getSystem().getString(R.string.registered_successfully))
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

    fun checkIfUserIsSignedIn() : Boolean {
        val currentUser : FirebaseUser? = auth.currentUser
        return currentUser != null
    }

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!
    }

    fun changePassword(email: String){
        auth.sendPasswordResetEmail(email)
    }

    suspend fun updatePassword(newPassword: String, confirmPassword: String): Boolean{
        if(newPassword == "" || confirmPassword == ""){
            makeToast(ScanMySkin.context.getString(R.string.password_must_not_be_empty))
        } else if(newPassword == confirmPassword && newPassword.length < 6){
            makeToast(ScanMySkin.context.getString(R.string.password_short))
        } else if(newPassword != confirmPassword){
            makeToast(ScanMySkin.context.getString(R.string.password_must_match))
        } else if(newPassword == confirmPassword){
            return try {
                getCurrentUser().updatePassword(newPassword).await()
                makeToast(ScanMySkin.context.getString(R.string.password_changed))
                true
            } catch (e: Exception){
                Log.d(TAG, e.message.toString())
                false
            }
        }
        return false
    }

    suspend fun deleteAccount(): Boolean{
        getCurrentUser().delete().await()
        makeToast(Resources.getSystem().getString(R.string.acccount_deleted))
        return true
    }
}