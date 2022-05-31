package com.example.scanmyskin.di

import com.example.scanmyskin.data.network.NetworkRequests
import com.example.scanmyskin.data.repository.AuthRepo
import com.example.scanmyskin.ui.fragments.viewmodels.AuthViewModel
import com.example.scanmyskin.ui.fragments.viewmodels.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
}

val viewModelModules = module{
    viewModel<AuthViewModel> { AuthViewModel() }
    viewModel<HomeViewModel> { HomeViewModel() }

    single { AuthRepo() }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient().newBuilder().addInterceptor(logging).build()
}

fun provideNetwork(retrofit: Retrofit): NetworkRequests = retrofit.create(NetworkRequests::class.java)