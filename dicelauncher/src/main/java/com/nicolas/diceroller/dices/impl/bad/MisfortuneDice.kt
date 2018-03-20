package com.nicolas.diceroller.dices.impl.bad

import com.nicolas.diceroller.dices.AbstractDice
import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.dices.Face.*

class MisfortuneDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            FAILURE,
            FAILURE,
            BANE,
            VOID,
            VOID,
            VOID
    )
}