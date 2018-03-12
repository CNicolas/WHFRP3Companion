package com.nicolas.whfrp3database.daos.player.playerLinked

import com.nicolas.whfrp3database.daos.Dao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.PlayerLinkedEntity

interface PlayerLinkedDao<E : PlayerLinkedEntity> : Dao<E> {
    fun add(entity: E, player: Player): E?

    fun findByNameAndPlayer(name: String, player: Player): E?
    fun findAllByPlayer(player: Player): List<E>

    fun updateByPlayer(entity: E, player: Player): E?

    fun delete(entity: E): Int
    fun deleteAllByPlayer(player: Player): Int
}