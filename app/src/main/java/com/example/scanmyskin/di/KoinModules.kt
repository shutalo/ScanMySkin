package com.example.scanmyskin.di

import com.example.scanmyskin.BuildConfig
import com.example.scanmyskin.data.repository.FirebaseRepo
import com.example.scanmyskin.helpers.ImageClassifier
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModules = module{
    viewModel<AuthViewModel> { AuthViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }
}

val repositoryModule = module {
    single { FirebaseRepo(androidApplication(),get(),get(),get(),get()) }
}

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance("gs://scanmyskin.appspot.com") }
    single { FirebaseVisionCloudDetectorOptions.Builder().setMaxResults(3).build()  }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

val appModules = module {
    val localModel = LocalModel.Builder()
        .setAssetManifestFilePath("manifest.json")
//        .setAssetFilePath("model")
        // or .setAbsoluteManifestFilePath(absolute file path to manifest file)
        .build() // Evaluate your model in the Cloud console
    val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.0f).build()

    val labeler = ImageLabeling.getClient(customImageLabelerOptions)

    factory { ImageClassifier(labeler) }
}