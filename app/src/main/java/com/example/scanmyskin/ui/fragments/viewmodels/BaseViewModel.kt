package com.example.scanmyskin.ui.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected var _shouldShowProgressDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    var shouldShowProgressDialog: LiveData<Boolean> = _shouldShowProgressDialog
}