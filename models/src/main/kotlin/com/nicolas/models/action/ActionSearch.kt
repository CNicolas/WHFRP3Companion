package com.nicolas.models.action

import java.io.Serializable

data class ActionSearch(val text: String,
                        val actionType: ActionType?,
                        val trait: Trait?,
                        val cooldown: Int?) : Serializable