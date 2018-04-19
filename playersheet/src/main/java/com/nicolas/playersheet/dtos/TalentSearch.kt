package com.nicolas.playersheet.dtos

import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType
import java.io.Serializable

data class TalentSearch(val text: String,
                        val talentType: TalentType?,
                        val cooldown: TalentCooldown?) : Serializable