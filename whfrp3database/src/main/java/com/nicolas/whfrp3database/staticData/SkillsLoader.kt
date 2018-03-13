package com.nicolas.whfrp3database.staticData

import android.content.Context
import com.beust.klaxon.Klaxon
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType

class SkillsLoader(context: Context) {
    val skills = loadSkills(context) ?: listOf()

    val basicSkills = skills.filter { it.type == SkillType.BASIC }
    val advancedSkills = skills.filter { it.type == SkillType.ADVANCED }
    val allSpecializations = skills.map { it to it.specializations }.toMap()

    private fun loadSkills(context: Context): List<Skill>? {
        return Klaxon().parseArray(context.assets.open("skills.json"))
    }
}