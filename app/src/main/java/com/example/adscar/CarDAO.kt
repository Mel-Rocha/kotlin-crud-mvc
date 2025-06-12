package com.example.adscar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CarDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "myApp.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CAR = "cars"
        private const val COLUMN_ID = "id"
        private const val COLUMN_BRAND = "brand"
        private const val COLUMN_MODEL = "model"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_PRICE = "price"
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

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Atualizações de schema (se necessário)
    }

    fun insertCar(car: Car): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BRAND, car.brand)
            put(COLUMN_MODEL, car.model)
            put(COLUMN_YEAR, car.year)
            put(COLUMN_PRICE, car.price)
        }
        val id = db.insert(TABLE_CAR, null, values)
        db.close()
        return id
    }

    fun getAllCars(): List<Car> {
        val cars = mutableListOf<Car>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CAR, null, null, null, null, null, "$COLUMN_BRAND ASC")

        while (cursor.moveToNext()) {
            cars.add(buildCar(cursor))
        }

        cursor.close()
        db.close()
        return cars
    }

    fun deleteCar(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_CAR, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun updateCar(car: Car): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BRAND, car.brand)
            put(COLUMN_MODEL, car.model)
            put(COLUMN_YEAR, car.year)
            put(COLUMN_PRICE, car.price)
        }
        return db.update(TABLE_CAR, values, "$COLUMN_ID = ?", arrayOf(car.id.toString()))
    }

    private fun buildCar(cursor: Cursor): Car {
        return Car(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)),
            model = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL)),
            year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)),
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
        )
    }
}
