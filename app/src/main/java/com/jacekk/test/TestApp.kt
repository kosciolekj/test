package com.jacekk.test

import android.app.Application
import com.jacekk.test.di.appModule
import com.jacekk.test.di.domainModule
import com.jacekk.test.di.networkModule
import com.jacekk.test.di.viewModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupKoin() = startKoin {
        androidContext(this@TestApp)
        modules(
            listOf(
                viewModule,
                domainModule,
                networkModule,
                appModule
            )
        )
    }
}