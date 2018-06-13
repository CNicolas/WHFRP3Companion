package com.nicolas.models.action.condition

import com.nicolas.models.item.enums.Range
import java.io.Serializable

data class ActionCondition(val weapon: ActionConditionWeapon? = null,

                           val range: Range? = null,

                           val target: Boolean? = null,
                           val self: Boolean? = null) : Serializable