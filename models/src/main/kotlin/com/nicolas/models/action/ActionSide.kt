package com.nicolas.models.action

import com.nicolas.models.action.effect.ActionEffects
import com.nicolas.models.dice.DiceType
import com.nicolas.models.player.enums.Stance
import java.io.Serializable

data class ActionSide(val stance: Stance,
                      val name: String? = null,
                      val conditionsText: String? = null,
                      val cooldown: Int? = null,
                      val difficulty: List<DiceType>? = null,
                      val effects: ActionEffects? = null,
                      val effectText: String? = null,
                      val specialText: String? = null) : Serializable {

    val cooldownString: String
        get() = when (cooldown) {
            null -> "!"
            else -> "$cooldown"
        }
}