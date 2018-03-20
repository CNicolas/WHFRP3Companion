package com.nicolas.diceroller.dices.impl.good

import com.nicolas.diceroller.dices.AbstractDice
import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.dices.Face.*

class CharacteristicDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            SUCCESS,
            SUCCESS,
            SUCCESS,
            SUCCESS,
            BOON,
            BOON,
            VOID,
            VOID
    )
}