package com.example.scanmyskin.di

import com.example.scanmyskin.BuildConfig
import com.example.scanmyskin.data.repository.FirebaseRepositoryImpl
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.linkfirebase.FirebaseModelSource
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModules = module {
    viewModel<AuthViewModel> { AuthViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
}

val repositoryModule = module {
    single { FirebaseRepositoryImpl(get(), get(), get(), get()) }
}

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance("gs://scanmyskin.appspot.com") }
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
//    val localModel = LocalModel.Builder()
//        .setAssetManifestFilePath("manifest.json")
//        .setAbsoluteFilePath("model.tflite")
//        // or .setAbsoluteManifestFilePath(absolute file path to manifest file)
//        .build() // Evaluate your model in the Cloud console
//    val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
//        .setConfidenceThreshold(0.0f).build()
//
//    val labeler = ImageLabeling.getClient(customImageLabelerOptions)

    val firebaseModelSource = FirebaseModelSource.Builder("skin")
        .build()
    factory { CustomRemoteModel.Builder(firebaseModelSource).build() }

//    factory { ImageClassifier(labeler) }
}