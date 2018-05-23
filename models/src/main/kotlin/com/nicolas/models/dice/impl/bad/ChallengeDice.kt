package com.nicolas.models.dice.impl.bad

import com.nicolas.models.dice.AbstractDice
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

class ChallengeDice : AbstractDice() {
    override val type = DiceType.CHALLENGE

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