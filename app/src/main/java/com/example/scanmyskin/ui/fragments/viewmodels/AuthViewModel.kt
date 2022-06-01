package com.example.scanmyskin.ui.fragments.viewmodels

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.repository.AuthRepo
import com.example.scanmyskin.helpers.isEmailValid
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepo) : ViewModel() {

    private val TAG = "AuthViewModel"
    private var _isUserRegisteredSuccessfully: MutableLiveData<Boolean> = MutableLiveData(false)
    var isUserRegisteredSuccessfully: LiveData<Boolean> = _isUserRegisteredSuccessfully
    private var _isPasswordChangeRequested: MutableLiveData<Boolean> = MutableLiveData(false)
    var isPasswordChangeRequested: LiveData<Boolean> = _isPasswordChangeRequested
    private var _isUserSignedIn: MutableLiveData<Boolean?> = MutableLiveData(null)
    var isUserSignedIn: LiveData<Boolean?> = _isUserSignedIn
    private var _isSigningInSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSigningInSuccessful: LiveData<Boolean> = _isSigningInSuccessful

    fun register(email: String, password: String){
        viewModelScope.launch {
            if(email.isEmailValid()){
                if(password.length < 8){
                    Toast.makeText(ScanMySkin.context,R.string.password_error, Toast.LENGTH_SHORT).show()
                } else {
                    _isUserRegisteredSuccessfully.postValue(repo.register(email,password))
                }
            } else {
                Toast.makeText(ScanMySkin.context,R.string.email_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            _isSigningInSuccessful.postValue(repo.signIn(email,password))
        }
    }

    fun changePassword(email: String){
        viewModelScope.launch {
            repo.changePassword(email)
            _isPasswordChangeRequested.postValue(true)
        }
    }

    fun getCurrentUser(): FirebaseUser {
        return repo.getCurrentUser()
    }
}