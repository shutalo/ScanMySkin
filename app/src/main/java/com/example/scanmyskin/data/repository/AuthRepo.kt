package com.example.scanmyskin.data.repository

import android.util.Log
import android.widget.Toast
import com.example.scanmyskin.ScanMySkin
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
            Log.d(TAG,"User added to database")
            isRegistrationSuccessful = true
            Toast.makeText(ScanMySkin.context, "Registered successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception){
            Log.d(TAG ,e.message.toString())
            Toast.makeText(ScanMySkin.context,e.message.toString(), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(ScanMySkin.context,"Wrong username or password",Toast.LENGTH_SHORT).show()
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

    suspend fun updatePassword(newPassword: String, confirmPassword: String){
        if(newPassword == "" || confirmPassword == ""){
            Toast.makeText(ScanMySkin.context,"Password entry must not be empty!", Toast.LENGTH_SHORT).show()
        } else if(newPassword == confirmPassword && newPassword.length < 6){
            Toast.makeText(ScanMySkin.context,"Password too short!", Toast.LENGTH_SHORT).show()
        } else if(newPassword != confirmPassword){
            Toast.makeText(ScanMySkin.context,"Password must match!", Toast.LENGTH_SHORT).show()
        } else if(newPassword == confirmPassword){
            getCurrentUser().updatePassword(newPassword).await()
            Toast.makeText(ScanMySkin.context,"Password changed successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun deleteAccount(): Boolean{
        getCurrentUser().delete().await()
        Toast.makeText(ScanMySkin.context,"Account deleted.",Toast.LENGTH_SHORT).show()
        return true
    }
}