package org.toch.rickmortytest

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.toch.rickmortytest.di.initKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin (
            appDeclaration = {
                androidLogger()
                androidContext(this@App)
            }
        )
    }
}