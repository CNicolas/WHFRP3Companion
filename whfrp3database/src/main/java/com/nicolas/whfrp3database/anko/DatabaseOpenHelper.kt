package com.nicolas.whfrp3database.anko

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.nicolas.whfrp3database.anko.tables.HAND_TABLE_NAME
import com.nicolas.whfrp3database.anko.tables.PLAYER_TABLE_NAME
import com.nicolas.whfrp3database.anko.tables.createHandTable
import com.nicolas.whfrp3database.anko.tables.createPlayerTable
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.dropTable

class DatabaseOpenHelper(private val context: Context) : ManagedSQLiteOpenHelper(context, "whfrp3.db", null, 1) {
    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        createHandTable(db)
        createPlayerTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(HAND_TABLE_NAME, true)
        db.dropTable(PLAYER_TABLE_NAME, true)
    }

    fun deleteDatabase() {
        context.deleteDatabase(databaseName)
    }
}

// Access property for Context
val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)