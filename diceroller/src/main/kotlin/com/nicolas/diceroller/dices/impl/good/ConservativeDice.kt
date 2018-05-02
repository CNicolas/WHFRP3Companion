package com.nicolas.diceroller.dices.impl.good

import com.nicolas.diceroller.dices.AbstractDice
import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.dices.Face.*

class ConservativeDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            SUCCESS,
            SUCCESS,
            SUCCESS,
            SUCCESS,
            BOON,
            BOON,
            SUCCESS_BOON,
            SUCCESS_DELAY,
            SUCCESS_DELAY,
            VOID
    )

    override fun roll(): List<Face> {
        val face = takeRandomFace()

        return when (face) {
            SUCCESS_BOON -> listOf(SUCCESS, BOON)
            SUCCESS_DELAY -> listOf(SUCCESS, DELAY)
            else -> listOf(face)
        }
    }
}