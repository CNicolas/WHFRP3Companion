package com.nicolas.dicelauncher.dices

import java.util.*

abstract class AbstractDice : Dice {
    private val facesCount: Int
        get() = faces.size

    override fun roll(): List<Face> = listOf(takeRandomFace())

    internal fun takeRandomFace() = faces[Random().nextInt(facesCount)]
}