package com.ikerfah.thmanyah

import android.app.Application
import com.ikerfah.thmanyah.di.appModule
import com.ikerfah.thmanyah.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, networkModule)
        }
    }
}