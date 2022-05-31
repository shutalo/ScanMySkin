package com.example.scanmyskin

import android.app.Application
import com.example.scanmyskin.di.appModules
import com.example.scanmyskin.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ScanMySkin : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        startKoin(){
            androidContext(this@ScanMySkin)
            modules(viewModelModules, appModules)
        }
    }

    companion object{
        lateinit var context : ScanMySkin
            private set
    }
}
