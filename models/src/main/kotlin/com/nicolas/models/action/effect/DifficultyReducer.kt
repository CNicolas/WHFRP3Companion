package com.nicolas.models.action.effect

import com.nicolas.models.dice.DiceType
import com.nicolas.models.item.enums.Range
import java.io.Serializable

data class DifficultyReducer(val range: Range? = null,
                             val target: Boolean? = null,
                             val player: Boolean? = null,
                             val ally: AllyModificationType? = null,
                             val dice: DiceType) : Serializable