package com.nicolas.diceroller.dices

interface Dice {
    val faces: List<Face>

    fun roll(): List<Face>
}