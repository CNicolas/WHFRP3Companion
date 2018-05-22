package com.nicolas.models.dice

import java.io.Serializable

interface Dice : Serializable {
    val faces: List<Face>

    fun roll(): List<Face>
}