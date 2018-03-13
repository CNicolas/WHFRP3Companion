package com.nicolas.dicelauncher.dices.impl.good

import com.nicolas.dicelauncher.dices.AbstractDice
import com.nicolas.dicelauncher.dices.Face
import com.nicolas.dicelauncher.dices.Face.*

class FortuneDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            SUCCESS,
            SUCCESS,
            BOON,
            VOID,
            VOID,
            VOID
    )
}