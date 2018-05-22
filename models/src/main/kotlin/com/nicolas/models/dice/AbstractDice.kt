package com.nicolas.models.dice

import java.util.*

abstract class AbstractDice : Dice {
    private val facesCount: Int
        get() = faces.size

    override fun roll(): List<Face> = listOf(takeRandomFace())

    fun takeRandomFace() = faces[Random().nextInt(facesCount)]
}