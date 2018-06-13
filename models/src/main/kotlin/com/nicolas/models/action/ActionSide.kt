package com.nicolas.models.action

import com.nicolas.models.action.effect.ActionEffects
import com.nicolas.models.action.effect.ActionSpecial
import java.io.Serializable

data class ActionSide(val effects: ActionEffects? = null,
                      val special: ActionSpecial? = null) : Serializable