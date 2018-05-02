package com.nicolas.models.player

import com.nicolas.models.hand.DifficultyLevel
import com.nicolas.models.hand.DifficultyLevel.NONE
import com.nicolas.models.hand.Hand
import java.io.Serializable

data class CharacteristicValue(val value: Int = 0, val fortuneValue: Int = 0) : Comparable<CharacteristicValue>, Serializable {
    fun getHand(name: String, difficultyLevel: DifficultyLevel = NONE) = Hand(
            name = name,
            characteristicDicesCount = value,
            fortuneDicesCount = fortuneValue,
            challengeDicesCount = difficultyLevel.challengeDicesCount
    )

    override fun compareTo(other: CharacteristicValue): Int {
        val valueDiff = value - other.value
        val fortuneDiff = fortuneValue - other.fortuneValue

        return when (valueDiff) {
            0 -> fortuneDiff
            else -> valueDiff
        }
    }
}