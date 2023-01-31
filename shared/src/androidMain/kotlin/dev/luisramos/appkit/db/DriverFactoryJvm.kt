package dev.luisramos.appkit.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.luisramos.ler.db.LerDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        val schema = LerDatabase.Schema
        return AndroidSqliteDriver(
            schema = schema,
            context = context,
            name = "ler.db",
            // LR: Speeds up database
            // https://stackoverflow.com/questions/65425352/sqldelight-slow-performance-compared-to-room
            callback = object : AndroidSqliteDriver.Callback(schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.query("PRAGMA JOURNAL_MODE = WAL").use { it.moveToFirst() }
                    db.query("PRAGMA SYNCHRONOUS = 2").use { it.moveToFirst() }
                }
            }
        )
    }
}