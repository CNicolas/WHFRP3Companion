package com.nicolas.models.action

import com.nicolas.models.action.condition.ActionCondition
import com.nicolas.models.player.enums.Stance
import com.nicolas.models.player.enums.Stance.CONSERVATIVE
import com.nicolas.models.player.enums.Stance.RECKLESS
import java.io.Serializable

data class Action(val name: String = "Action",
                  val type: ActionType,
                  val traits: List<Trait> = listOf(),

                  val skill: String? = null,
                  val targetSkill: String? = null,
                  val targetDefense: Boolean? = null,
                  val skillText: String? = null,
                  val conditions: List<ActionCondition>? = null,
                  val conditionsText: String? = null,

                  val conservativeSide: ActionSide,
                  val recklessSide: ActionSide) : Serializable {

    val skillsString: String?
        get() = when {
            skillText != null -> skillText
            skill != null -> when {
                targetSkill != null -> "$skill contre $targetSkill de la cible"
                targetDefense != null -> "$skill contre Defense de la cible"
                else -> skill
            }
            else -> null
        }

    val conditionsString: String?
        get() = when {
            conditionsText != null -> conditionsText
            conditions != null -> conditions.joinToString(". ")
            else -> null
        }

    fun getNameByStance(side: Stance? = null): String =
            when (side) {
                CONSERVATIVE -> conservativeSide.name?.let { it }
                RECKLESS -> conservativeSide.name?.let { it }
                else -> null
            } ?: name

    fun getConditionsStringByStance(side: Stance? = null): String? =
            when (side) {
                CONSERVATIVE -> conservativeSide.conditionsText?.run { this }
                RECKLESS -> conservativeSide.conditionsText?.run { this }
                else -> null
            } ?: conditionsString
}
