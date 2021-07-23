package com.example.excursionstest

import android.app.Application
import com.example.excursionstest.di.repositoryModule
import com.example.excursionstest.di.useCaseModule
import com.example.excursionstest.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ExcursionsTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExcursionsTestApplication)
            modules(listOf(repositoryModule, useCaseModule, viewModelModule))
        }
    }
}
