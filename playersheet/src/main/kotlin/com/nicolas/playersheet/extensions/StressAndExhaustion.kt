package com.nicolas.playersheet.extensions

import com.nicolas.models.player.Player
import com.nicolas.playersheet.enums.ExhaustionState
import com.nicolas.playersheet.enums.ExhaustionState.*
import com.nicolas.playersheet.enums.StressState
import com.nicolas.playersheet.enums.StressState.*

// region STRESS

fun Player.addStress(stressPoints: Int): Player {
    stress += stressPoints

    return this
}

fun Player.removeStress(stressPoints: Int): Player {
    val newStressValue = when {
        stress - stressPoints <= 0 -> 0
        else -> stress - stressPoints
    }
    stress = newStressValue

    return this
}

fun Player.stressState(): StressState =
        when {
            stress >= maxStress -> FAINTED
            stress >= maxStress / 2 -> STRESSED
            else -> NOT_STRESSED
        }

// endregion

// region EXHAUSTION

fun Player.addExhaustion(exhaustionPoints: Int): Player {
    exhaustion += exhaustionPoints

    return this
}

fun Player.removeExhaustion(exhaustionPoints: Int): Player {
    val newExhaustionValue = when {
        exhaustion - exhaustionPoints <= 0 -> 0
        else -> exhaustion - exhaustionPoints
    }
    exhaustion = newExhaustionValue

    return this
}

fun Player.exhaustionState(): ExhaustionState =
        when {
            exhaustion >= maxExhaustion -> COMA
            exhaustion >= maxExhaustion / 2 -> EXHAUSTED
            else -> NOT_EXHAUSTED
        }

// endregion