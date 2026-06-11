package org.toch.rickmortytest.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        // Crea una base de datos local en un archivo llamado "rickmorty.db"
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:rickmorty.db")

        // Crea las tablas automáticamente si no existen
        RickAndMortyDatabaseTest.Schema.create(driver)
        return driver
    }
}