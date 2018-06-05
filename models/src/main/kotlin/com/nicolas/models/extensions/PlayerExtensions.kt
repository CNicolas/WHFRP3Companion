package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.PlayerLivingState

fun Player.earnExperiencePoints(experiencePoints: Int): Player {
    experience += experiencePoints
    maxExperience += experiencePoints

    return this
}

val Player.livingState: PlayerLivingState
    get() {
        val woundsBeforeDying = maxWounds - wounds
        return when {
            woundsBeforeDying < 0 -> PlayerLivingState.DEAD
            woundsBeforeDying == 0 -> PlayerLivingState.KO
            woundsBeforeDying <= maxWounds / 2 -> PlayerLivingState.HEAVILY_INJURED
            woundsBeforeDying < maxWounds -> PlayerLivingState.SLIGHTLY_INJURED
            else -> PlayerLivingState.UNINJURED
        }
    }

fun Player.receiveDamage(damage: Int): Pair<Int, PlayerLivingState> {
    val maximumDamage = damage - soak - toughness.value + effects.sumBy { it.damageTakenModifier ?: 0 }
    val damageDealt = when {
        maximumDamage <= 0 -> 1
        else -> maximumDamage
    }

    return damageDealt to loseHealth(damageDealt)
}

fun Player.loseHealth(damage: Int): PlayerLivingState {
    wounds += damage
    return livingState
}

fun Player.heal(damage: Int): PlayerLivingState {
    val newWoundsValue = when {
        wounds - damage <= 0 -> 0
        else -> wounds - damage
    }
    wounds = newWoundsValue

    return livingState
}