package com.nicolas.diceroller.dices.impl.bad

import com.nicolas.diceroller.dices.AbstractDice
import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.dices.Face.*

class ChallengeDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            FAILURE,
            FAILURE,
            FAILURE_FAILURE,
            FAILURE_FAILURE,
            BANE,
            BANE_BANE,
            CHAOS,
            VOID
    )

    override fun roll(): List<Face> {
        val face = takeRandomFace()

        return when (face) {
            FAILURE_FAILURE -> listOf(FAILURE, FAILURE)
            BANE_BANE -> listOf(BANE, BANE)
            else -> listOf(face)
        }
    }
}