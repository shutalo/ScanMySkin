package com.example.scanmyskin

import android.app.Application
import com.example.scanmyskin.di.firebaseModule
import com.example.scanmyskin.di.networkModule
import com.example.scanmyskin.di.repositoryModule
import com.example.scanmyskin.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ScanMySkin : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        startKoin(){
            androidContext(this@ScanMySkin)
            modules(viewModelModules, networkModule, repositoryModule, firebaseModule)
        }
    }

    companion object{
        lateinit var context : ScanMySkin
            private set
    }
}
