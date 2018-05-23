package com.nicolas.models.dice.impl.good

import com.nicolas.models.dice.AbstractDice
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

class RecklessDice : AbstractDice() {
    override val type = DiceType.RECKLESS

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