package com.example.adscar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelperCar(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "adscar_cars.db"
        const val DATABASE_VERSION = 1

        const val TABLE_CAR = "Car"
        const val COLUMN_ID = "id"
        const val COLUMN_BRAND = "brand"
        const val COLUMN_MODEL = "model"
        const val COLUMN_YEAR = "year"
        const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCarTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_CAR (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BRAND TEXT NOT NULL,
                $COLUMN_MODEL TEXT NOT NULL,
                $COLUMN_YEAR INTEGER NOT NULL,
                $COLUMN_PRICE REAL NOT NULL
            )
        """.trimIndent()
        db.execSQL(createCarTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CAR")
        onCreate(db)
    }
}
