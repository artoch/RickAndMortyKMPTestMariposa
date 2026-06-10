package org.toch.rickmortytest.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.toch.rickmortytest.data.local.DriverFactory

actual val platformModule = module {
    single<SqlDriver> {
        // 💡 Usamos tu DriverFactory pasándole el contexto que Koin ya tiene
        DriverFactory(context = androidContext()).createDriver()
    }
}