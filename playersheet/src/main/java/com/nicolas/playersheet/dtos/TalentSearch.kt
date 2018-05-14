package com.nicolas.playersheet.dtos

import com.nicolas.models.player.playerLinked.talent.TalentCooldown
import com.nicolas.models.player.playerLinked.talent.TalentType
import java.io.Serializable

data class TalentSearch(val text: String,
                        val talentType: TalentType?,
                        val cooldown: TalentCooldown?) : Serializable