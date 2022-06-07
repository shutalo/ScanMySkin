package com.example.scanmyskin.ui.activities

import android.os.Bundle
import com.example.scanmyskin.TensorFlow.Classifier

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.databinding.ActivityHomeBinding
import com.example.scanmyskin.databinding.ActivityLoginBinding
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import com.example.scanmyskin.ui.fragments.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), EasyPermissions.PermissionCallbacks{

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    override val viewModel: HomeViewModel by viewModel()

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE_PERMISSIONS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        askForPermissions()
        NavigationUI.setupWithNavController(binding.bottomNavigationView,findNavController(R.id.fragmentContainer))
    }

    private fun askForPermissions(){
        if(EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.d(TAG,"permissions already granted!",)
            Toast.makeText(this,ScanMySkin.context.getString(R.string.permission_granted_already), Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(this, ScanMySkin.context.getString(R.string.permission_explanation), REQUEST_CODE_PERMISSIONS, android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG,"permissions granted!",)
        Toast.makeText(this,ScanMySkin.context.getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            Log.d(TAG,"permissions denied!",)
            AppSettingsDialog.Builder(this).build().show()
            Toast.makeText(this,ScanMySkin.context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
        }
    }
}