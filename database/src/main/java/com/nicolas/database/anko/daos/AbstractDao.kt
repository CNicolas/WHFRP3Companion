package com.nicolas.database.anko.daos

import android.database.Cursor
import com.nicolas.database.anko.DatabaseOpenHelper
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select

internal abstract class AbstractDao<out E>(protected val databaseHelper: DatabaseOpenHelper) : Dao<E> {
    abstract val tableName: String

    override fun findById(id: Int): E? = databaseHelper.writableDatabase
            .select(tableName)
            .whereArgs("id = $id")
            .exec {
                return@exec parse(this)
            }

    override fun deleteAll(): Int = databaseHelper.writableDatabase
            .delete(tableName)

    protected abstract fun parse(cursor: Cursor): E?
    protected abstract fun parseAll(cursor: Cursor): List<E>
}