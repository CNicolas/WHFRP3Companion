package com.nicolas.models.action.condition

import com.nicolas.models.action.effect.Target
import com.nicolas.models.action.effect.Target.*
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.Range.*
import java.io.Serializable

data class ActionCondition(val weapon: ActionConditionWeapon? = null,
                           val characteristic: ActionConditionCharacteristic? = null,
                           val range: Range? = null,
                           val target: Target? = null,
                           val preparation: Boolean? = null,
                           val encumbrance: Boolean? = null,
                           val energy: Int? = null,
                           val wounds: Int? = null) : Serializable {

    override fun toString(): String {
        return listOfNotNull(weapon.toString(), characteristic.toString(), rangeString, encumbranceString, energyString)
                .joinToString(", ")
                .capitalize()
    }

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
                NONE, PLAYER -> when (range) {
                    ENGAGED -> "désengagé"
                    SHORT -> "aucun ennemi à courte portée"
                    MEDIUM -> "aucun ennemi à moyenne portée"
                    LONG -> "aucun ennemi à longue portée"
                    EXTREME -> "aucun ennemi à portée extrême"
                }
                else -> null
            }
        }

    private val encumbranceString: String?
        get() = when (encumbrance) {
            true -> "encombré"
            false -> "non encombré"
            else -> null
        }

    private val energyString: String?
        get() = when (energy) {
            null -> null
            else -> "$energy énergie"
        }
}