package com.example.scanmyskin.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private var _shouldShowProgressDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    var shouldShowProgressDialog: LiveData<Boolean> = _shouldShowProgressDialog

    fun shouldShowProgressDialog(value: Boolean){
        _shouldShowProgressDialog.postValue(value)
    }
}