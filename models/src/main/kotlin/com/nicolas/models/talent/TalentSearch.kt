package com.nicolas.models.talent

import java.io.Serializable

data class TalentSearch(val text: String,
                        val talentType: TalentType?,
                        val cooldown: TalentCooldown?) : Serializable