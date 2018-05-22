package com.nicolas.playersheet.dtos

import com.nicolas.models.player.talent.TalentCooldown
import com.nicolas.models.player.talent.TalentType
import java.io.Serializable

data class TalentSearch(val text: String,
                        val talentType: TalentType?,
                        val cooldown: TalentCooldown?) : Serializable