package org.toch.rickmortytest.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest

actual fun createInMemorySqlDriver(): SqlDriver {
    // El driver nativo de iOS para pruebas en memoria
    return inMemoryDriver(RickAndMortyDatabaseTest.Schema)
}