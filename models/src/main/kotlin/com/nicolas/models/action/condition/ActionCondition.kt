package com.nicolas.models.action.condition

import com.nicolas.models.action.effect.Target
import com.nicolas.models.action.effect.Target.ALLY
import com.nicolas.models.action.effect.Target.TARGET
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.Range.*
import java.io.Serializable

data class ActionCondition(val weapon: ActionConditionWeapon? = null,
                           val range: Range? = null,
                           val target: Target? = null) : Serializable {

    override fun toString(): String =
            listOfNotNull(weapon.toString(), rangeString)
                    .joinToString(", ")
                    .capitalize()

    private val rangeString: String?
        get() {
            if (range == null) {
                return null
            }

            return when (target) {
                TARGET -> when (range) {
                    ENGAGED -> "engagé avec la cible"
                    SHORT -> "jusqu'à courte portée de la cible"
                    MEDIUM -> "jusqu'à moyenne portée de la cible"
                    LONG -> "jusqu'à longue portée de la cible"
                    EXTREME -> "jusqu'à portée extrême de la cible"
                }
                ALLY -> when (range) {
                    ENGAGED -> "engagé avec un allié"
                    SHORT -> "jusqu'à courte portée d'un allié"
                    MEDIUM -> "jusqu'à moyenne portée d'un allié"
                    LONG -> "jusqu'à longue portée d'un allié"
                    EXTREME -> "jusqu'à portée extrême d'un allié"
                }
                else -> when (range) {
                    ENGAGED -> "désengagé"
                    SHORT -> "aucun ennemi à courte portée"
                    MEDIUM -> "aucun ennemi à moyenne portée"
                    LONG -> "aucun ennemi à longue portée"
                    EXTREME -> "aucun ennemi à portée extrême"
                }
            }
        }
}