package org.toch.rickmortytest.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        // 💡 Cambiado a RickAndMortyDatabaseTest
        return NativeSqliteDriver(RickAndMortyDatabaseTest.Companion.Schema, "rickandmorty.db")
    }
}