package com.example.scanmyskin

import android.app.Application
import com.example.scanmyskin.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ScanMySkin : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        startKoin(){
            androidContext(this@ScanMySkin)
            modules(viewModelModules, networkModule, repositoryModule, firebaseModule, dataModule)
        }
    }

    companion object{
        lateinit var context : ScanMySkin
            private set
    }
}
