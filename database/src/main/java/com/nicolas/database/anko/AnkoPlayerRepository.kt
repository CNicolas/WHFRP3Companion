package com.nicolas.database.anko

import android.content.Context
import com.nicolas.database.PlayerRepository
import com.nicolas.database.anko.daos.player.PlayerDao
import com.nicolas.database.loadActions
import com.nicolas.database.loadSkills
import com.nicolas.models.action.Trait
import com.nicolas.models.player.Player
import com.nicolas.models.skill.SkillType

class AnkoPlayerRepository(context: Context) : PlayerRepository {
    private val playerDao = PlayerDao(context.database)

    private val basicSkills = loadSkills(context).filter { it.type == SkillType.BASIC }
    private val basicActions = loadActions(context).filter { it.traits.contains(Trait.BASIC) }

    override fun add(player: Player): Player {
        player.createSkills()
        player.createActions()
        playerDao.add(player)

        return find(player.name)!!
    }

    override fun find(name: String): Player? = playerDao.findByName(name)

    override fun findAll(): List<Player> = playerDao.findAll()

    override fun update(player: Player): Player {
        playerDao.update(player)

        return find(player.name)!!
    }

    override fun delete(name: String): Int {
        val player = playerDao.findByName(name)
        return if (player != null) {
            playerDao.deleteByName(name)
        } else {
            -1
        }
    }

    override fun delete(player: Player): Int {
        val foundPlayer = playerDao.findById(player.id)
        return if (foundPlayer != null) {
            playerDao.delete(foundPlayer)
        } else {
            delete(player.name)
        }
    }

    private fun Player.createSkills() {
        skills = basicSkills.toList()
    }

    private fun Player.createActions() {
        actions = basicActions.toList()
    }
}