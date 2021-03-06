package com.nicolas.models.skill

import com.nicolas.models.player.enums.Characteristic
import java.io.Serializable

data class Skill(val name: String,
                 val characteristic: Characteristic,
                 val type: SkillType = SkillType.BASIC,
                 var level: Int = 0,
                 var specializations: List<Specialization> = listOf()) : Serializable