package com.nicolas.whfrp3database.daos.player.playerLinked

import com.nicolas.whfrp3database.DatabaseOpenHelper
import com.nicolas.whfrp3database.daos.AbstractDao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.PlayerLinkedEntity
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

abstract class AbstractPlayerLinkedDao<E : PlayerLinkedEntity>(databaseHelper: DatabaseOpenHelper)
    : AbstractDao<E>(databaseHelper), PlayerLinkedDao<E> {

    override fun add(entity: E, player: Player): E? = databaseHelper.writableDatabase.let {
        it.insert(tableName, *getColumns(entity, player))

        return findByNameAndPlayer(entity.name, player)
    }

    override fun findByNameAndPlayer(name: String, player: Player): E? {
        return findAllByPlayer(player).firstOrNull { it.name == name }
    }

    override fun findAllByPlayer(player: Player): List<E> {
        return databaseHelper.writableDatabase.let {
            it.select(tableName)
                    .whereArgs("playerId = ${player.id}")
                    .exec {
                        return@exec parseAll(this)
                    }
        }
    }

    override fun updateByPlayer(entity: E, player: Player): E? {
        val id = databaseHelper.writableDatabase
                .update(tableName, *getColumns(entity, player))
                .whereArgs("id = ${entity.id}")
                .exec()

        return findById(id)
    }

    override fun deleteAllByPlayer(player: Player): Int = databaseHelper.writableDatabase
            .delete(tableName, "playerId = ${player.id}")

    override fun delete(entity: E): Int = databaseHelper.writableDatabase
            .delete(tableName, "id = ${entity.id}")


    abstract fun getColumns(entity: E, player: Player): Array<Pair<String, Any?>>
}