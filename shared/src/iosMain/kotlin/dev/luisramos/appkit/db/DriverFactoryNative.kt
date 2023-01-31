package dev.luisramos.appkit.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.luisramos.ler.db.LerDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(LerDatabase.Schema, "ler.db")
    }
}