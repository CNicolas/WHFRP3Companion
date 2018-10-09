package com.nicolas.whfrp3companion.playersheet.skills

import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization

interface SkillListener {
    fun skillLevelHandler(skill: Skill, level: Int)
    fun skillSecondaryHandler(skill: Skill) {}

    fun specializationToggleHandler(skill: Skill, specialization: Specialization)
    fun specializationSecondaryHandler(skill: Skill, specialization: Specialization) {}
}