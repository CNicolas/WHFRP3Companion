package com.nicolas.whfrp3database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.nicolas.whfrp3database.tables.*
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.select

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
        createPlayerTable(db)
        createItemTable(db)
        createHandTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(PLAYER_TABLE_NAME, true)
        db.dropTable(ITEM_TABLE_NAME, true)
        db.dropTable(HAND_TABLE_NAME, true)
    }

    fun deleteDatabase() {
        context.deleteDatabase(databaseName)
    }

    fun nextAvailableId(db: SQLiteDatabase, tableName: String): Int {
        return db.select(tableName)
                .orderBy("id", SqlOrderDirection.DESC)
                .exec {
                    if (this.count == 0) {
                        return@exec 1
                    }
                    return@exec getInt(0) + 1
                }
    }
}

// Access property for Context
val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)