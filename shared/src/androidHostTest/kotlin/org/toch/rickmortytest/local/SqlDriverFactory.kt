package org.toch.rickmortytest.local;

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest

actual fun createInMemorySqlDriver(): SqlDriver {
    return AndroidSqliteDriver(
        schema = RickAndMortyDatabaseTest.Schema,
        context = androidx.test.core.app.ApplicationProvider.getApplicationContext(),
        name = null // 👈 ESTA ES LA CLAVE: 'null' le dice a Android que la monte en la RAM
    )
}