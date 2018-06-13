package com.nicolas.models.action.effect

import java.io.Serializable

data class ActionFaceEffect(val damage: Int? = null,
                            val ignoreSoak: Boolean? = null,
                            val cooldown: Int? = null,
                            val exhaustion: Int? = null,
                            val stress: Int? = null,
                            val maneuver: Int? = null,
                            val canEngage: Target? = null,
                            val canDisengage: Target? = null) : Serializable