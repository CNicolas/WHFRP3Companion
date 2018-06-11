package com.nicolas.models.dice.impl.good

import com.nicolas.models.dice.AbstractDice
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

class FortuneDice : AbstractDice() {
    override val type = DiceType.FORTUNE

    override val faces: List<Face> = listOf(
            SUCCESS,
            SUCCESS,
            BOON,
            VOID,
            VOID,
            VOID
    )
}