package com.nicolas.whfrp3database

import android.content.Context
import com.nicolas.whfrp3database.daos.player.PlayerDao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.staticData.loadSkills
import com.nicolas.whfrp3database.staticData.loadTalents

class PlayerFacade(context: Context) {
    private val playerDao = PlayerDao(context.database)

    val skills = loadSkills(context)
    val basicSkills = skills.filter { it.type == SkillType.BASIC }
    val advancedSkills = skills.filter { it.type == SkillType.ADVANCED }
    val allSpecializations = skills.map { it to it.specializations }.toMap()

    val talents = loadTalents(context)
    val passiveTalents = talents.filter { it.cooldown == TalentCooldown.PASSIVE }
    val exhaustibleTalents = talents.filter { it.cooldown == TalentCooldown.TALENT }

    fun add(player: Player): Player {
        player.createSkills()
        playerDao.add(player)

        return find(player.name)!!
    }

    fun find(name: String): Player? = playerDao.findByName(name)

    fun findAll(): List<Player> = playerDao.findAll()

    fun update(player: Player): Player {
        playerDao.update(player)

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

    private fun Player.createSkills() {
        skills = basicSkills.toList()
    }
}