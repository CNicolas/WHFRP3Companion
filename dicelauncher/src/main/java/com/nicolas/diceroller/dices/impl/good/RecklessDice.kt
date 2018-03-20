package com.nicolas.diceroller.dices.impl.good

import com.nicolas.diceroller.dices.AbstractDice
import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.dices.Face.*

class RecklessDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            SUCCESS_SUCCESS,
            SUCCESS_SUCCESS,
            SUCCESS_BOON,
            BOON_BOON,
            SUCCESS_EXHAUSTION,
            SUCCESS_EXHAUSTION,
            BANE,
            BANE,
            VOID,
            VOID
    )

    override fun roll(): List<Face> {
        val face = takeRandomFace()

        return when (face) {
            SUCCESS_SUCCESS -> listOf(SUCCESS, SUCCESS)
            SUCCESS_BOON -> listOf(SUCCESS, BOON)
            BOON_BOON -> listOf(BOON, BOON)
            SUCCESS_EXHAUSTION -> listOf(SUCCESS, EXHAUSTION)
            else -> listOf(face)
        }
    }
}