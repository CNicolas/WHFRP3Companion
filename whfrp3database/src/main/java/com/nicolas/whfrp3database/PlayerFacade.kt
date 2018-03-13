package com.nicolas.whfrp3database

import android.content.Context
import com.nicolas.whfrp3database.daos.player.PlayerDao
import com.nicolas.whfrp3database.daos.player.playerLinked.item.ItemDao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.staticData.loadSkills
import com.nicolas.whfrp3database.staticData.loadTalents

class PlayerFacade(context: Context) {
    private val playerDao = PlayerDao(context.database)
    private val itemDao = ItemDao(context.database)

    val skills = loadSkills(context) ?: listOf()
    val basicSkills = skills.filter { it.type == SkillType.BASIC }
    val advancedSkills = skills.filter { it.type == SkillType.ADVANCED }
    val allSpecializations = skills.map { it to it.specializations }.toMap()

    val talents = loadTalents(context) ?: listOf()
    val passiveTalents = talents.filter { it.cooldown == TalentCooldown.PASSIVE }
    val exhaustibleTalents = talents.filter { it.cooldown == TalentCooldown.TALENT }

    fun add(player: Player): Player {
        createSkillsForPlayer(player)
        val savedPlayer = playerDao.add(player)

        savedPlayer!!.items = player.items
        updateItems(savedPlayer)

        return find(player.name)!!
    }

    fun find(name: String): Player? = playerDao.findByName(name).fillItems()

    fun findAll(): List<Player> {
        val players = playerDao.findAll()
        players.forEach {
            it.fillItems()
        }

        return players
    }

    fun update(player: Player): Player {
        playerDao.update(player)
        updateItems(player)

        setPlayersLists(player)

        return find(player.name)!!
    }

    fun deletePlayer(name: String) {
        val player = playerDao.findByName(name)
        if (player != null) {
            playerDao.deleteByName(name)
        }
    }

    fun deletePlayer(player: Player) {
        val foundPlayer = playerDao.findById(player.id)
        if (foundPlayer != null) {
            playerDao.delete(foundPlayer)
        } else {
            deletePlayer(player.name)
        }
    }

    fun deleteAll() {
        playerDao.deleteAll()
    }

    private fun Player?.fillItems(): Player? {
        if (this != null) {
            this.items = itemDao.findAllByPlayer(this)
        }
        return this
    }

    private fun setPlayersLists(player: Player) {
        player.items = findAllItemsByPlayer(player)
    }

    private fun updateItems(player: Player) {
        val savedItems = findAllItemsByPlayer(player).toMutableList()

        player.items.forEach { it ->
            val item = itemDao.findById(it.id)
            savedItems.remove(item)

            if (item == null) {
                itemDao.add(it, player)!!
            } else {
                itemDao.updateByPlayer(it, player)!!
            }
        }

        savedItems.forEach { itemDao.delete(it) }
    }

    private fun findAllItemsByPlayer(player: Player) = itemDao.findAllByPlayer(player)

    private fun createSkillsForPlayer(player: Player) {
        basicSkills.forEach {
            val mutableSkills = player.skills.toMutableList()
            mutableSkills.add(it)
            player.skills = mutableSkills
        }
    }
}