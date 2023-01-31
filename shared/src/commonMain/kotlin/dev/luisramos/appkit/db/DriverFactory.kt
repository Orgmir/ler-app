package dev.luisramos.appkit.db

import app.cash.sqldelight.db.SqlDriver
import app.luisramos.ler.db.LerDatabase

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): LerDatabase {
    val driver = driverFactory.createDriver()
    return LerDatabase(driver)
}