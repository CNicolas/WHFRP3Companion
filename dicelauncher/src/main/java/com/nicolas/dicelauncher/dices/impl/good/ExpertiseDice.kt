package com.nicolas.dicelauncher.dices.impl.good

import com.nicolas.dicelauncher.dices.AbstractDice
import com.nicolas.dicelauncher.dices.Face
import com.nicolas.dicelauncher.dices.Face.*

class ExpertiseDice : AbstractDice() {
    override val faces: List<Face> = listOf(
            SUCCESS,
            SUCCESS_PLUS,
            BOON,
            BOON,
            SIGMAR,
            VOID
    )

    override fun roll(): List<Face> {
        val face = takeRandomFace()

        return when (face) {
            SUCCESS_PLUS -> reroll(listOf(SUCCESS))
            else -> listOf(face)
        }
    }

    private fun reroll(currentFaces: List<Face>): List<Face> {
        val face = takeRandomFace()

        return when (face) {
            SUCCESS_PLUS -> reroll(currentFaces + SUCCESS)
            else -> currentFaces + face
        }
    }
}