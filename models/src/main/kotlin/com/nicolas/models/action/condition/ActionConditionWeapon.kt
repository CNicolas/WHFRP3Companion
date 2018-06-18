package com.nicolas.models.action.condition

import com.nicolas.models.item.enums.WeaponCategory
import java.io.Serializable

data class ActionConditionWeapon(val categories: List<WeaponCategory>? = listOf(),
                                 val oneHanded: Boolean? = null,
                                 val twoHanded: Boolean? = null,
                                 val equipped: Boolean? = null) : Serializable {
    override fun toString(): String {
        return super.toString()
    }
}