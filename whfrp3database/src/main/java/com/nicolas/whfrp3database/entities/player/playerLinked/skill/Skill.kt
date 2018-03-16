package com.nicolas.whfrp3database.entities.player.playerLinked.skill

import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import java.io.Serializable

data class Skill(val name: String,
                 val characteristic: Characteristic,
                 val type: SkillType = SkillType.BASIC,
                 var level: Int = 0,
                 var specializations: List<Specialization> = listOf()) : Serializable