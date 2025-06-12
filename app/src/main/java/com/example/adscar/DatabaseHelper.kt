package com.example.adscar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// This class creates and manages the local SQLite database
class DatabaseHelper(
    context: Context,
    DATABASE_NAME: String,
    factory: SQLiteDatabase.CursorFactory?,
    DATABASE_VERSION: Int
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(50) NOT NULL UNIQUE,
                cpf VARCHAR(11),
                password VARCHAR(30),
                birthDate DATE,
                accessLevel INTEGER
            )
        """.trimIndent()
        db.execSQL(createUserTable)

        // Insert a default admin user for initial testing (can be removed later)
        val insertAdminUser = """
            INSERT INTO User (id, name, email, cpf, password, birthDate, accessLevel)
            VALUES (0, 'Admin', 'admin@company.com', '12345678901', 'adminpass', '1965-05-08', 1)
        """.trimIndent()
        db.execSQL(insertAdminUser)
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }

    // Companion object defines the database name and version
    companion object {
        const val DATABASE_NAME = "adscar.db"
        const val DATABASE_VERSION = 1
    }
}
