package com.coding.openweatherapp

import android.app.Application
import com.coding.openweatherapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class OpenWeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@OpenWeatherApplication)
            modules(appModule)
        }
    }
}