package com.nicolas.models.action.effect

import java.io.Serializable

data class ActionSpecial(val difficultyReducer: DifficultyReducer,
                         val text: String,
                         val text2: String? = null) : Serializable