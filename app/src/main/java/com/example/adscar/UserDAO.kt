package com.example.adscar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.mindrot.jbcrypt.BCrypt
import java.text.SimpleDateFormat
import java.util.*

class UserDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "myApp.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_CPF = "cpf"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_BIRTH_DATE = "birthDate"
        private const val COLUMN_ACCESS_LEVEL = "accessLevel"
    }

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME VARCHAR(100) NOT NULL,
                $COLUMN_EMAIL VARCHAR(50) NOT NULL UNIQUE,
                $COLUMN_CPF VARCHAR(11),
                $COLUMN_PASSWORD VARCHAR(60),
                $COLUMN_BIRTH_DATE DATE,
                $COLUMN_ACCESS_LEVEL INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_CPF, user.cpf)
            put(COLUMN_PASSWORD, BCrypt.hashpw(user.password, BCrypt.gensalt()))
            put(COLUMN_BIRTH_DATE, user.birthDate)
            put(COLUMN_ACCESS_LEVEL, user.accessLevel)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteUser(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun updateUser(user: User): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_CPF, user.cpf)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_BIRTH_DATE, dateFormat.format(user.birthDate))
            put(COLUMN_ACCESS_LEVEL, user.accessLevel)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(user.id.toString()))
    }

    fun findUserByEmail(email: String): User? {
        val db = readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CPF, COLUMN_PASSWORD, COLUMN_BIRTH_DATE, COLUMN_ACCESS_LEVEL)
        val cursor = db.query(TABLE_NAME, projection, "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = buildUser(cursor)
        }
        cursor.close()
        return user
    }

    fun findUserByName(name: String): User? {
        val db = readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CPF, COLUMN_PASSWORD, COLUMN_BIRTH_DATE, COLUMN_ACCESS_LEVEL)
        val cursor = db.query(TABLE_NAME, projection, "$COLUMN_NAME = ?", arrayOf(name), null, null, null)

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = buildUser(cursor)
        }
        cursor.close()
        return user
    }

    fun findUserByEmailAndPassword(email: String, password: String): User? {
        val db = readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CPF, COLUMN_PASSWORD, COLUMN_BIRTH_DATE, COLUMN_ACCESS_LEVEL)
        val cursor = db.query(TABLE_NAME, projection, "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)

        var user: User? = null
        if (cursor.moveToFirst()) {
            val storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            if (BCrypt.checkpw(password, storedPassword)) {
                user = buildUser(cursor)
            }
        }
        cursor.close()
        return user
    }

    fun searchUsersByValue(value: String): List<User> {
        val db = readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CPF, COLUMN_PASSWORD, COLUMN_BIRTH_DATE, COLUMN_ACCESS_LEVEL)
        val selection = "$COLUMN_EMAIL LIKE ? OR $COLUMN_CPF LIKE ? OR $COLUMN_PASSWORD LIKE ? OR $COLUMN_BIRTH_DATE LIKE ? OR $COLUMN_ACCESS_LEVEL = ?"
        val args = arrayOf("%$value%", "%$value%", "%$value%", "%$value%", value)
        val cursor = db.query(TABLE_NAME, projection, selection, args, null, null, null)

        val users = mutableListOf<User>()
        while (cursor.moveToNext()) {
            users.add(buildUser(cursor))
        }
        cursor.close()
        return users
    }

    private fun buildUser(cursor: Cursor): User {
        return User(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
            cpf = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CPF)),
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
            birthDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE)),
            accessLevel = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCESS_LEVEL))
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle DB upgrades here
    }

    fun selectAll(): ArrayList<User> {
        val users = ArrayList<User>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            users.add(buildUser(cursor))
        }

        cursor.close()
        db.close()
        return users
    }
}
