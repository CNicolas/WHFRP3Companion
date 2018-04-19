package com.nicolas.playersheet.dtos

import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType

data class TalentSearch(val name: String = "",
                        val description: String = "",
                        val talentType: TalentType? = null,
                        val cooldown: TalentCooldown? = null,
                        val characteristic: Characteristic? = null,
                        val skill: Skill? = null)