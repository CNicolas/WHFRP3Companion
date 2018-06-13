package com.nicolas.models.action.effect

import java.io.Serializable

data class ActionEffects(val success: ActionFaceEffectByCount? = null,
                         val boon: ActionFaceEffectByCount? = null,
                         val sigmar: ActionFaceEffectByCount? = null,
                         val bane: ActionFaceEffectByCount? = null,
                         val failure: ActionFaceEffectByCount? = null,
                         val chaos: ActionFaceEffectByCount? = null,
                         val exhaustion: ActionFaceEffectByCount? = null) : Serializable