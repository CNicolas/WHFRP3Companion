package com.nicolas.models.action.effect

import com.nicolas.models.action.effect.Target.*
import java.io.Serializable
import java.lang.Math.abs

data class ActionFaceEffect(val damage: Int? = null,
                            val critical: Int? = null,
                            val ignoreSoak: Boolean? = null,
                            val cooldown: Int? = null,
                            val exhaustion: Int? = null,
                            val stress: Int? = null,
                            val maneuver: Boolean? = null,
                            val canEngage: Target? = null,
                            val canDisengage: Target? = null) : Serializable {

    override fun toString(): String {
        return listOfNotNull(damageString,
                criticalString,
                ignoreSoakString,
                cooldownString,
                exhaustionString,
                stressString,
                maneuverString,
                canEngageString,
                canDisengageString).joinToString(", ")
    }

    private val damageString: String?
        get() {
            val res = "Vous infligez les dégâts"

            return when {
                damage == null -> null
                damage == 0 -> "$res normaux"
                damage < 0 -> "$res $damage"
                damage > 0 -> "$res +$damage"
                else -> null
            }
        }

    private val criticalString: String?
        get() {
            val res = "critique"

            return when {
                critical == null -> null
                critical < 0 -> "$critical $res"
                critical > 0 -> "+$critical $res"
                else -> null
            }
        }

    private val ignoreSoakString: String?
        get() = when (ignoreSoak) {
            true -> "ignorez l'encaissement"
            else -> null
        }

    private val cooldownString: String?
        get () {
            val res = "jetons de recharge à cette carte"

            return when {
                cooldown == null || cooldown == 0 -> null
                cooldown < 0 -> "Enlevez ${abs(cooldown)} res"
                cooldown > 0 -> "Ajoutez $cooldown $res"
                else -> null
            }
        }

    private val exhaustionString: String?
        get () {
            return when {
                exhaustion == null || exhaustion == 0 -> null
                exhaustion < 0 -> "Subissez ${abs(exhaustion)} fatigue"
                exhaustion > 0 -> "Récupérez $exhaustion fatigue"
                else -> null
            }
        }

    private val stressString: String?
        get () {
            return when {
                stress == null || stress == 0 -> null
                stress < 0 -> "Subissez ${abs(stress)} stress"
                stress > 0 -> "Récupérez $stress stress"
                else -> null
            }
        }

    private val maneuverString: String?
        get() = when (maneuver) {
            true -> "Vous pouvez exécuter une manoeuvre supplémentaire"
            else -> null
        }

    private val canEngageString: String?
        get() = when (canEngage) {
            TARGET -> "Un ennemi à courte portée peut vous engager"
            PLAYER -> "Vous pouvez engager un ennemi à courte portée"
            ALLY -> "Vous pouvez engager un allié à courte portée"
            else -> null
        }

    private val canDisengageString: String?
        get() = when (canDisengage) {
            TARGET -> "Un ennemi engagé avec vous peut se désengager"
            PLAYER -> "Vous pouvez vous désengager d'un ennemi"
            ALLY -> "Vous pouvez vous désengager d'un allié"
            else -> null
        }
}