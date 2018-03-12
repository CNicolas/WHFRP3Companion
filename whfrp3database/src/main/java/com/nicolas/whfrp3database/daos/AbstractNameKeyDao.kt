package com.nicolas.whfrp3database.daos

import com.nicolas.whfrp3database.DatabaseOpenHelper
import com.nicolas.whfrp3database.entities.NamedEntity
import com.nicolas.whfrp3database.tables.parsers.toPairs
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

abstract class AbstractNameKeyDao<E : NamedEntity>(databaseHelper: DatabaseOpenHelper)
    : AbstractDao<E>(databaseHelper), NameKeyDao<E> {

    override fun add(entity: E): E? = databaseHelper.writableDatabase.let {
        val columns = getColumns(entity).toMutableMap()
        if (entity.id == -1) {
            columns["id"] = databaseHelper.nextAvailableId(it, tableName)
        }

        val id = it.insert(tableName, *columns.toPairs())

        return findByName(entity.name)
    }

    override fun findByName(name: String): E? = databaseHelper.writableDatabase.let {
        it.select(tableName)
                .whereArgs("name = '$name'")
                .exec {
                    return@exec parse(this)
                }
    }

    override fun findAll(): List<E> = databaseHelper.writableDatabase.let {
        it.select(tableName).exec {
            return@exec parseAll(this)
        }
    }

    override fun update(entity: E): E? {
        val id = databaseHelper.writableDatabase
                .update(tableName, *getColumns(entity).toPairs())
                .whereArgs("id = '${entity.id}'")
                .exec()

        return findById(id)
    }

    override fun delete(entity: E): Int = databaseHelper.writableDatabase
            .delete(tableName, "id = '${entity.id}'")

    override fun deleteByName(name: String): Int = databaseHelper.writableDatabase
            .delete(tableName, "name = '$name'")

    abstract fun getColumns(entity: E): Map<String, Any?>
}