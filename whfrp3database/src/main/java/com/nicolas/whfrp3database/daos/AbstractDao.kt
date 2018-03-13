package com.nicolas.whfrp3database.daos

import android.database.Cursor
import com.nicolas.whfrp3database.DatabaseOpenHelper
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select

internal abstract class AbstractDao<E>(protected val databaseHelper: DatabaseOpenHelper) : Dao<E> {
    abstract val tableName: String

    override fun findById(id: Int): E? =
            databaseHelper.writableDatabase.let {
                it.select(tableName).exec {
                    return@exec parse(this)
                }
            }

    override fun deleteAll(): Int = databaseHelper.writableDatabase
            .delete(tableName)

    protected abstract fun parse(cursor: Cursor): E?
    protected abstract fun parseAll(cursor: Cursor): List<E>
}