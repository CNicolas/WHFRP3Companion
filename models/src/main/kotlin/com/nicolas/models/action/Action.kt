package com.nicolas.models.action

import com.nicolas.models.action.condition.ActionCondition
import com.nicolas.models.dice.DiceType
import java.io.Serializable

data class Action(val name: String,
                  val type: String,
                  val conservativeSide: ActionSide,
                  val recklessSide: ActionSide,

                  val traits: List<Trait> = listOf(),
                  val cooldown: Int? = null,
                  val difficulty: List<DiceType>? = null,

                  val conditions: List<ActionCondition>? = null,

                  val skill: String? = null,
                  val targetSkill: String? = null,
                  val targetDefense: Boolean? = null,

                  val description: String = "") : Serializable
