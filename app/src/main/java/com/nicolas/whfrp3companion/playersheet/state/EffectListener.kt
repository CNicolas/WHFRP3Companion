package com.nicolas.whfrp3companion.playersheet.state

import com.nicolas.models.effect.Effect

interface EffectListener {
    fun onAddEffect(effect: Effect)
    fun onRemoveEffect(effect: Effect)
}
