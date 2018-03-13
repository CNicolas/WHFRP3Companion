package com.nicolas.dicelauncher.dices

interface Dice {
    val faces: List<Face>

    fun roll(): List<Face>
}