package com.example.scanmyskin.ui.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.repository.AuthRepo
import com.example.scanmyskin.helpers.isEmailValid
import com.example.scanmyskin.helpers.isPasswordValid
import com.example.scanmyskin.helpers.makeToast
import com.example.scanmyskin.helpers.validateRegistrationInput
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepo) : BaseViewModel() {

    private val TAG = "AuthViewModel"
    private var _isUserRegisteredSuccessfully: MutableLiveData<Boolean> = MutableLiveData(false)
    var isUserRegisteredSuccessfully: LiveData<Boolean> = _isUserRegisteredSuccessfully
    private var _isPasswordChangeRequested: MutableLiveData<Boolean> = MutableLiveData(false)
    var isPasswordChangeRequested: LiveData<Boolean> = _isPasswordChangeRequested
    private var _isUserSignedIn: MutableLiveData<Boolean?> = MutableLiveData(null)
    var isUserSignedIn: LiveData<Boolean?> = _isUserSignedIn
    private var _isSigningInSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSigningInSuccessful: LiveData<Boolean> = _isSigningInSuccessful
    private var _isPasswordChangedSuccessfully: MutableLiveData<Boolean> = MutableLiveData(false)
    var isPasswordChangedSuccessfully: LiveData<Boolean> = _isPasswordChangedSuccessfully

    fun register(email: String, password: String, confirmedPassword: String){
        viewModelScope.launch {
            if(validateRegistrationInput(email,password,confirmedPassword)){
                shouldShowProgressDialog(true)
                _isUserRegisteredSuccessfully.postValue(repo.register(email,password))
            }
        }
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            shouldShowProgressDialog(true)
            _isSigningInSuccessful.postValue(repo.signIn(email,password))
        }
    }

    fun requestPasswordChange(email: String){
        viewModelScope.launch {
            if(email.isEmailValid()){
                _isPasswordChangeRequested.postValue(repo.changePassword(email))
            } else {
                makeToast(ScanMySkin.context.getString(R.string.email_error))
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String){
        viewModelScope.launch {
            shouldShowProgressDialog(true)
            _isPasswordChangedSuccessfully.postValue(repo.updatePassword(oldPassword,newPassword))
        }
    }

    fun getCurrentUser(): FirebaseUser {
        return repo.getCurrentUser()
    }

    fun shouldShowProgressDialog(value: Boolean){
        _shouldShowProgressDialog.postValue(value)
    }
}