package org.toch.rickmortytest.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.dsl.module
import org.toch.rickmortytest.data.local.DriverFactory

actual val platformModule = module {
    single<SqlDriver> {
        // En iOS no pide contexto en el constructor
        DriverFactory().createDriver()
    }
}