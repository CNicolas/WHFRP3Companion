package com.nicolas.models.action.effect

import com.nicolas.models.action.ActionType
import com.nicolas.models.action.effect.Target.*
import com.nicolas.models.dice.DiceType
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.or
import com.nicolas.models.or
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.plus
import java.io.Serializable
import java.lang.Math.abs

data class ActionFaceEffect(val damage: Int? = null,
                            val critical: Int? = null,
                            val ignoreSoak: Boolean? = null,
                            val energy: Int? = null,
                            val favor: Int? = null,
                            val cooldown: Int? = null,
                            val exhaustion: Int? = null,
                            val stress: Int? = null,
                            val wounds: Int? = null,
                            val maneuver: Boolean? = null,
                            val canEngage: Target? = null,
                            val canDisengage: Target? = null,
                            val addedDices: List<DiceType>? = null,
                            val actionType: List<ActionType>? = null,
                            val target: Target? = null,
                            val range: Range? = null,
                            val characteristics: List<Characteristic>? = null,

                            val text: String? = null) : Serializable {

    override fun toString(): String = text ?: listOfNotNull(damageString,
            criticalString,
            ignoreSoakString,
            energyString,
            favorString,
            cooldownString,
            stressString,
            exhaustionString,
            woundsString,
            maneuverString,
            canEngageString,
            canDisengageString)
            .joinToString()
            .capitalize()

    private val damageString: String?
        get() {
            val res = "vous infligez les dégâts"

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

    private val energyString: String?
        get() = when {
            energy == null -> null
            energy > 0 -> "Regagnez $energy après avoir lancé ce sort"
            energy < 0 -> "Perdez $energy après avoir lancé ce sort"
            else -> null
        }

    private val favorString: String?
        get() = when {
            favor == null -> null
            favor > 0 -> "Regagnez $favor après avoir lancé ce sort"
            favor < 0 -> "Perdez $favor après avoir lancé ce sort"
            else -> null
        }

    private val cooldownString: String?
        get () {
            val res = "jetons de recharge à cette carte"

            return when {
                cooldown == null || cooldown == 0 -> null
                cooldown < 0 -> "enlevez ${abs(cooldown)} $res"
                cooldown > 0 -> "ajoutez $cooldown $res"
                else -> null
            }
        }

    private val exhaustionString: String?
        get () {
            return when {
                exhaustion == null || exhaustion == 0 -> null
                exhaustion == 1 -> "subissez ${abs(exhaustion)} fatigue"
                exhaustion > 1 -> "subissez ${abs(exhaustion)} fatigues"
                exhaustion == -1 -> "récupérez $exhaustion fatigue"
                exhaustion > -1 -> "récupérez $exhaustion fatigues"
                else -> null
            }
        }

    private val stressString: String?
        get () {
            return when {
                stress == null || stress == 0 -> null
                stress > 0 -> "subissez ${abs(stress)} stress"
                stress < 0 -> "récupérez $stress stress"
                else -> null
            }
        }

    private val woundsString: String?
        get () {
            return when {
                wounds == null || wounds == 0 -> null
                wounds == -1 -> "subissez ${abs(wounds)} blessure"
                wounds < 0 -> "subissez ${abs(wounds)} blessures"
                wounds == 1 -> "subissez ${abs(wounds)} blessure"
                wounds > 0 -> "récupérez $wounds blessures"
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

    operator fun plus(other: ActionFaceEffect): ActionFaceEffect =
            ActionFaceEffect(
                    damage = damage + other.damage,
                    critical = critical + other.critical,
                    ignoreSoak = ignoreSoak.or(other.ignoreSoak),
                    energy = energy + other.energy,
                    favor = favor + other.favor,
                    cooldown = cooldown + other.cooldown,
                    exhaustion = exhaustion + other.exhaustion,
                    stress = stress + other.stress,
                    wounds = wounds + other.wounds,
                    maneuver = maneuver.or(other.maneuver),
                    canEngage = canEngage.or(other.canEngage),
                    canDisengage = canDisengage.or(other.canDisengage),
                    addedDices = addedDices + other.addedDices,
                    actionType = actionType + other.actionType,
                    target = target.or(other.target),
                    range = range.or(other.range),
                    characteristics = characteristics + other.characteristics
            )
}