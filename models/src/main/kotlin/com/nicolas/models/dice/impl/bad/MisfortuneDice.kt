package com.nicolas.models.dice.impl.bad

import com.nicolas.models.dice.AbstractDice
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

class MisfortuneDice : AbstractDice() {
    override val type = DiceType.MISFORTUNE

    override val faces: List<Face> = listOf(
            FAILURE,
            FAILURE,
            BANE,
            VOID,
            VOID,
            VOID
    )
}