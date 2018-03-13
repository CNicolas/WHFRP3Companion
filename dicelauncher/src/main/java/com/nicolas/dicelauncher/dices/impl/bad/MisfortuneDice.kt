package warhammer.dicelauncher.dices.impl.bad

import com.nicolas.dicelauncher.dices.AbstractDice
import com.nicolas.dicelauncher.dices.Face
import com.nicolas.dicelauncher.dices.Face.*

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