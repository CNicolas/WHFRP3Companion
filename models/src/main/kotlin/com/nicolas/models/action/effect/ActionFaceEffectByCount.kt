package com.nicolas.models.action.effect

import java.io.Serializable

data class ActionFaceEffectByCount(val one: ActionFaceEffect? = null,
                                   val two: ActionFaceEffect? = null,
                                   val three: ActionFaceEffect? = null,
                                   val four: ActionFaceEffect? = null) : Serializable