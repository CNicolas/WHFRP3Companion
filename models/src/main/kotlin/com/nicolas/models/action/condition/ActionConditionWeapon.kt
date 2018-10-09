package com.nicolas.models.action.condition

import com.nicolas.models.item.enums.WeaponCategory
import com.nicolas.models.item.enums.WeaponCategory.*
import java.io.Serializable

data class ActionConditionWeapon(val categories: List<WeaponCategory> = listOf(),
                                 val oneHanded: Boolean? = null,
                                 val twoHanded: Boolean? = null,
                                 val equipped: Boolean? = null,
                                 val inHand: Boolean? = null) : Serializable {
    override fun toString(): String =
            listOfNotNull(categoriesString, weaponHandlingString, equippedString, inHandString)
                    .joinToString(" ")
                    .capitalize()

    private val categoriesString: String?
        get() {
            val categoriesTextList = categories.map {
                when (it) {
                    MELEE -> "arme de mêlée"
                    RANGE -> "arme à distance"
                    FIRE_ARM -> "arme à feu"
                    REPEATING -> "arme à répétition"
                    THROWING -> "arme de jet"
                    SHIELD -> "bouclier"
                }
            }

            return categoriesTextList.joinToString()
        }

    private val weaponHandlingString: String?
        get() {
            return when {
                oneHanded == true -> "à une main"
                twoHanded == true -> "à deux mains"
                else -> null
            }
        }

    private val equippedString: String?
        get() = when (equipped) {
            true -> "équipée"
            else -> null
        }

    private val inHandString: String?
        get() = when (inHand) {
            true -> "en main"
            else -> null
        }
}

fun ActionConditionWeapon?.toString(): String? {
    return this?.toString()
}