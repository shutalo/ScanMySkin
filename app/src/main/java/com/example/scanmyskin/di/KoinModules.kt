package com.example.scanmyskin.di

import com.example.scanmyskin.BuildConfig
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

val viewModelModules = module{
    viewModel<AuthViewModel> { AuthViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel() }
}

val repositoryModule = module {
    single { AuthRepo() }
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