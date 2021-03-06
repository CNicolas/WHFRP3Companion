package com.nicolas.database.anko.daos

import com.nicolas.database.anko.DatabaseOpenHelper
import com.nicolas.database.toPairs
import com.nicolas.models.NamedEntity
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

internal abstract class AbstractNameKeyDao<E : NamedEntity>(databaseHelper: DatabaseOpenHelper)
    : AbstractDao<E>(databaseHelper), NameKeyDao<E> {

    override fun add(entity: E): E? {
        val nextId: Int = (findAll().map { it.id }.max() ?: 0) + 1

        val columns = getColumns(entity).toMutableMap()
        if (entity.id == -1) {
            columns["id"] = nextId
        }

        databaseHelper.writableDatabase.insert(tableName, *columns.toPairs())

        return findByName(entity.name)
    }

    override fun findByName(name: String): E? = databaseHelper.writableDatabase
            .select(tableName)
            .whereArgs("name = '$name'")
            .exec {
                return@exec parse(this)
            }

    override fun findAll(): List<E> = databaseHelper.writableDatabase
            .select(tableName)
            .exec {
                return@exec parseAll(this)
            }

    override fun update(entity: E): E? {
        databaseHelper.writableDatabase
                .update(tableName, *getColumns(entity).toPairs())
                .whereArgs("id = ${entity.id} or name = '${entity.name}'")
                .exec()

        return findByName(entity.name)
    }

    override fun delete(entity: E): Int = databaseHelper.writableDatabase
            .delete(tableName, "id = ${entity.id} or name = '${entity.name}'")

    override fun deleteByName(name: String): Int = databaseHelper.writableDatabase
            .delete(tableName, "name = '$name'")

    protected abstract fun getColumns(entity: E): Map<String, Any?>
}