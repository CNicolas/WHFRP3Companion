package com.nicolas.models.dice.impl.good

import com.nicolas.models.dice.AbstractDice
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

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