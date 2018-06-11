package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization

fun Player.addSkill(skill: Skill): List<Skill> {
    val mutableSkills = skills.toMutableList()
    mutableSkills.add(skill)
    skills = mutableSkills.toList()

    return skills
}

fun Player.getSkillByName(name: String): Skill? =
        skills.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }

fun Player.getSkillsByCharacteristic(characteristic: Characteristic): List<Skill> =
        skills.filter { it.characteristic == characteristic }

fun Player.getSpecializations(): List<Specialization> =
        skills.flatMap {
            it.getSpecializedSpecializations()
        }

fun Player.getSpecializationByName(name: String): Specialization? =
        getSpecializations().firstOrNull { it.name == name }


fun Skill.getSpecializedSpecializations(): List<Specialization> =
        specializations.filter { it.isSpecialized }

fun Skill.getSpecializationByName(name: String): Specialization? =
        specializations.firstOrNull { it.name == name }