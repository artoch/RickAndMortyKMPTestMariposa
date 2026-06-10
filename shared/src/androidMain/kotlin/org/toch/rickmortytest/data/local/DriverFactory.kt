package org.toch.rickmortytest.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            RickAndMortyDatabaseTest.Companion.Schema,
            context,
            "rickandmorty.db"
        )
    }
}