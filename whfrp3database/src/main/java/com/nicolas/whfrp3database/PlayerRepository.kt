package com.nicolas.whfrp3database

import com.nicolas.models.player.Player

interface PlayerRepository {
    fun add(player: Player): Player

    fun find(name: String): Player?
    fun findAll(): List<Player>

    fun update(player: Player): Player

    fun delete(name: String): Int
    fun delete(player: Player): Int
}