package com.nicolas.models.action.condition

import com.nicolas.models.player.enums.Characteristic
import java.io.Serializable

data class ActionConditionCharacteristic(val characteristic: Characteristic,
                                         val comparator: ActionConditionComparator = ActionConditionComparator.EQUAL,
                                         val value: Int = 0) : Serializable {
    override fun toString(): String {
        val characteristicCapitalized = characteristic.name.toLowerCase().capitalize()

        return "$characteristicCapitalized $valueString"
    }

    private val valueString: String
        get() = when (comparator) {
            ActionConditionComparator.LESS_OR_EQUAL -> "au plus $value"
            ActionConditionComparator.GREATER_OR_EQUAL -> "$value+"
            else -> "$value"
        }
}

fun ActionConditionCharacteristic?.toString(): String? {
    return this?.toString()
}