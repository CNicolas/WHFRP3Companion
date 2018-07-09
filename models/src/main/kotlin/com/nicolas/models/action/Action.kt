package com.nicolas.models.action

import com.nicolas.models.action.condition.ActionCondition
import java.io.Serializable

data class Action(val name: String,
                  val type: ActionType,
                  val traits: List<Trait> = listOf(),

                  val skill: String? = null,
                  val targetSkill: String? = null,
                  val targetDefense: Boolean? = null,
                  val conditions: List<ActionCondition>? = null,

                  val conservativeSide: ActionSide,
                  val recklessSide: ActionSide) : Serializable {
    val skillsString: String?
        get() = when {
            skill != null -> when {
                targetSkill != null -> "$skill contre $targetSkill de la cible"
                else -> "$skill contre Defense de la cible"
            }
            else -> null
        }

    val conditionsString: String?
        get() = when {
            conditions != null -> conditions.joinToString(". ")
            else -> ""
        }
}
