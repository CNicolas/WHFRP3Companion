package com.nicolas.whfrp3companion.playersheet.skills

import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization

interface SkillListener {
    fun skillSecondaryHandler(skill: Skill) {}

    fun specializationSecondaryHandler(skill: Skill, specialization: Specialization) {}
}