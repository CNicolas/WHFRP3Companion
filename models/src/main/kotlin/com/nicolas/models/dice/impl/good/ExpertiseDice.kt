package com.nicolas.models.dice.impl.good

import com.nicolas.models.dice.AbstractDice
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

class ExpertiseDice : AbstractDice() {
    override val type = DiceType.EXPERTISE

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