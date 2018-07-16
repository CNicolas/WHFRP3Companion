package com.nicolas.models.action

import com.nicolas.models.action.effect.ActionEffects
import com.nicolas.models.action.effect.ActionSpecial
import com.nicolas.models.dice.DiceType
import com.nicolas.models.player.enums.Stance
import java.io.Serializable

data class ActionSide(val stance: Stance,
                      val cooldown: Int? = null,
                      val difficulty: List<DiceType>? = null,
                      val effects: ActionEffects? = null,
                      val special: ActionSpecial? = null) : Serializable