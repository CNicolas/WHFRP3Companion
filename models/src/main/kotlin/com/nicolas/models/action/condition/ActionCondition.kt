package com.nicolas.models.action.condition

import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.Range.*
import java.io.Serializable

data class ActionCondition(val weapon: ActionConditionWeapon? = null,

                           val range: Range? = null,

                           val target: Boolean? = null,
                           val self: Boolean? = null) : Serializable {
    override fun toString(): String {
        return weapon?.toString() ?: if (range != null) {
            if (target == true) {
                when (range) {
                    ENGAGED -> "Engagé avec la cible"
                    SHORT -> "Jusqu'à courte portée de la cible"
                    MEDIUM -> "Jusqu'à moyenne portée de la cible"
                    LONG -> "Jusqu'à longue portée de la cible"
                    EXTREME -> "Jusqu'à portée extrême de la cible"
                }
            } else {
                when (range) {
                    ENGAGED -> "Désengagé"
                    SHORT -> "Aucun ennemi à courte portée"
                    MEDIUM -> "Aucun ennemi à moyenne portée"
                    LONG -> "Aucun ennemi à longue portée"
                    EXTREME -> "Aucun ennemi à portée extrême"
                }
            }
        } else {
            ""
        }
    }
}