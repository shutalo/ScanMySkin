package com.example.scanmyskin.ui.fragments.auth

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.databinding.FragmentStartingBinding
import com.example.scanmyskin.helpers.ImageClassifier
import com.example.scanmyskin.helpers.shortDelay
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.linkfirebase.FirebaseModelSource
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.concurrent.schedule

class StartingFragment : BaseFragment<FragmentStartingBinding>() {

    private val TAG = "StartingFragment"
    private val viewModel by sharedViewModel<AuthViewModel>()
    private val remoteModel by inject<CustomRemoteModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartingBinding
        get() = FragmentStartingBinding::inflate

    override fun setupUi(){
        viewModel.checkIfUserIsSignedIn()
        retrieveModel()
    }

    private fun updateUI(){
        viewModel.isUserSignedIn.observe(this) {
            Timer().schedule(500) {
                activity?.runOnUiThread {
                    if (it) {
                        activity?.finish()
                        val extras = ActivityNavigator.Extras.Builder()
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .build()
                        findNavController().navigate(
                            StartingFragmentDirections.actionStartingFragmentToHomeActivity(),
                            extras
                        )
                    } else {
                        with(binding) {
                            authenticationFormLayout.visibility = View.VISIBLE
                            YoYo.with(Techniques.SlideInUp).playOn(authenticationFormLayout)
                            register.setOnClickListener { button ->
                                YoYo.with(Techniques.Pulse).duration(shortDelay).onEnd {
                                    findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToRegistrationFragment())
                                }.playOn(button)
                            }
                            login.setOnClickListener { button ->
                                YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd {
                                    findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToLoginFragment())
                                }.playOn(button)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun retrieveModel(){
        val downloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        RemoteModelManager.getInstance().isModelDownloaded(remoteModel)
            .addOnSuccessListener {
                if(it){
                    Log.d(TAG, "model available")
                    updateUI()
                } else {
                    RemoteModelManager.getInstance().download(remoteModel, downloadConditions)
                        .addOnSuccessListener {
                            Log.d(TAG,"model retrieved")
                            updateUI()
                        }
                        .addOnFailureListener{ ex ->
                            Log.d(TAG,"model retrieve failed.")
                            Log.d(TAG,ex.toString())
                        }
                }
            }

    }
}