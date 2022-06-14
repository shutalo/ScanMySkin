package com.example.scanmyskin.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.scanmyskin.helpers.SingleLiveEvent

open class BaseViewModel : ViewModel() {

    private var _shouldShowProgressDialog: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var shouldShowProgressDialog: LiveData<Boolean> = _shouldShowProgressDialog

    fun shouldShowProgressDialog(value: Boolean){
        _shouldShowProgressDialog.postValue(value)
    }
}