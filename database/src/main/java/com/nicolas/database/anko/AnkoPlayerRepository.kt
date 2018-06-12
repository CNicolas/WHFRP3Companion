package com.nicolas.database.anko

import android.content.Context
import com.nicolas.database.PlayerRepository
import com.nicolas.database.anko.daos.player.PlayerDao
import com.nicolas.database.loadSkills
import com.nicolas.database.loadTalents
import com.nicolas.models.player.Player
import com.nicolas.models.player.skill.SkillType
import com.nicolas.models.player.talent.TalentCooldown

class AnkoPlayerRepository(context: Context) : PlayerRepository {
    private val playerDao = PlayerDao(context.database)

    val skills = loadSkills(context)
    private val basicSkills = skills.filter { it.type == SkillType.BASIC }
    val advancedSkills = skills.filter { it.type == SkillType.ADVANCED }
    val allSpecializations = skills.map { it to it.specializations }.toMap()

    val talents = loadTalents(context)
    val passiveTalents = talents.filter { it.cooldown == TalentCooldown.PASSIVE }
    val exhaustibleTalents = talents.filter { it.cooldown == TalentCooldown.TALENT }

    override fun add(player: Player): Player {
        player.createSkills()
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
}