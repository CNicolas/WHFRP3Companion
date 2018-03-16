package com.nicolas.whfrp3database.entities.player.playerLinked.talent

import java.io.Serializable

data class Talent(val name: String,
                  val cooldown: TalentCooldown,
                  val type: TalentType,
                  val description: String,
                  var isEquipped: Boolean = false) : Serializable