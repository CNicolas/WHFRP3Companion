package com.nicolas.whfrp3database.daos.player.playerLinked

import com.nicolas.whfrp3database.DatabaseOpenHelper
import com.nicolas.whfrp3database.daos.AbstractDao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.PlayerLinkedEntity
import com.nicolas.whfrp3database.tables.parsers.toPairs
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

internal abstract class AbstractPlayerLinkedDao<E : PlayerLinkedEntity>(databaseHelper: DatabaseOpenHelper)
    : AbstractDao<E>(databaseHelper), PlayerLinkedDao<E> {

    override fun add(entity: E, player: Player): E? {
        val nextId: Int = (findAll().map { it.id }.max() ?: 0) + 1

        val columns = getColumns(entity, player).toMutableMap()
        if (entity.id == -1) {
            columns["id"] = nextId
        }

        databaseHelper.writableDatabase.insert(tableName, *getColumns(entity, player).toPairs())

        return findByNameAndPlayer(entity.name, player)
    }

    override fun findByNameAndPlayer(name: String, player: Player): E? {
        return findAllByPlayer(player).firstOrNull { it.name == name }
    }

    override fun findAllByPlayer(player: Player): List<E> = databaseHelper.writableDatabase.let {
        it.select(tableName)
                .whereArgs("playerId = ${player.id}")
                .exec {
                    return@exec parseAll(this)
                }
    }

    private fun findAll(): List<E> = databaseHelper.writableDatabase.let {
        it.select(tableName)
                .exec {
                    return@exec parseAll(this)
                }
    }

    override fun updateByPlayer(entity: E, player: Player): E? {
        databaseHelper.writableDatabase
                .update(tableName, *getColumns(entity, player).toPairs())
                .whereArgs("playerId = ${player.id} and (id = ${entity.id} or name = '${entity.name}')")
                .exec()

        return findByNameAndPlayer(entity.name, player)
    }

    override fun deleteAllByPlayer(player: Player): Int = databaseHelper.writableDatabase
            .delete(tableName, "playerId = ${player.id}")

    override fun delete(entity: E): Int = databaseHelper.writableDatabase
            .delete(tableName, "id = ${entity.id}")


    abstract fun getColumns(entity: E, player: Player): Map<String, Any?>
}