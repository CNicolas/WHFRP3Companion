package com.nicolas.models.hand

import com.nicolas.models.NamedEntity
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.DiceType.*

data class Hand(override var name: String,
                var characteristicDicesCount: Int = 0,
                var expertiseDicesCount: Int = 0,
                var fortuneDicesCount: Int = 0,
                var conservativeDicesCount: Int = 0,
                var recklessDicesCount: Int = 0,
                var challengeDicesCount: Int = 0,
                var misfortuneDicesCount: Int = 0,

                override val id: Int = -1) : NamedEntity {

    operator fun get(diceType: DiceType) =
            when (diceType) {
                CHARACTERISTIC -> characteristicDicesCount
                EXPERTISE -> expertiseDicesCount
                FORTUNE -> fortuneDicesCount
                CONSERVATIVE -> conservativeDicesCount
                RECKLESS -> recklessDicesCount
                CHALLENGE -> challengeDicesCount
                MISFORTUNE -> misfortuneDicesCount
            }

    operator fun set(diceType: DiceType, value: Int) {
        when (diceType) {
            CHARACTERISTIC -> characteristicDicesCount = value
            EXPERTISE -> expertiseDicesCount = value
            FORTUNE -> fortuneDicesCount = value
            CONSERVATIVE -> conservativeDicesCount = value
            RECKLESS -> recklessDicesCount = value
            CHALLENGE -> challengeDicesCount = value
            MISFORTUNE -> misfortuneDicesCount = value
        }
    }
}
