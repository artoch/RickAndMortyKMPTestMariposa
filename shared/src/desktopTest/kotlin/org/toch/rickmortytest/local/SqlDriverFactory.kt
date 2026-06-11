package org.toch.rickmortytest.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest

actual fun createInMemorySqlDriver(): SqlDriver {
    // El prefijo ":memory:" le dice a JDBC que cree la base de datos en la RAM
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    // ⚠️ CRUCIAL: En JDBC, una base de datos en memoria nace completamente vacía.
    // Debes forzar la creación de las tablas ejecutando el Schema antes de devolverlo.
    RickAndMortyDatabaseTest.Schema.create(driver)

    return driver
}