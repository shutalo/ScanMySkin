package com.example.scanmyskin.di

import com.example.scanmyskin.BuildConfig
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.data.repository.FirebaseRepo
import com.example.scanmyskin.ui.adapters.DiseaseRecyclerViewAdapter
import com.example.scanmyskin.ui.viewmodels.AuthViewModel
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModules = module{
    viewModel<AuthViewModel> { AuthViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
}

val repositoryModule = module {
    single { FirebaseRepo(get(),get(),get()) }
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
//    single { get<Retrofit>().create(GithubSearchApi::class.java) }
}

//fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//    return Retrofit.Builder().baseUrl("").client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create()).build()
//}
//
//fun provideOkHttpClient(): OkHttpClient {
//    val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
//    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//    return OkHttpClient().newBuilder().addInterceptor(logging).build()
//}
//
//fun provideNetwork(retrofit: Retrofit): NetworkRequests = retrofit.create(NetworkRequests::class.java)