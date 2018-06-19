package com.nicolas.models.action.effect

import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*

typealias ActionEffects = Map<Face, ActionFaceEffectByCount>

val ActionEffects.effectsCount: Int
    get() = this[SUCCESS].size +
            this[BOON].size +
            this[SIGMAR].size +
            this[BANE].size +
            this[FAILURE].size +
            this[CHAOS].size +
            this[EXHAUSTION].size

operator fun ActionEffects.get(index: Int): ActionEffect? {
    if (index >= effectsCount || index < 0) {
        return null
    }

    return getEffectsList()[index]
}

fun ActionEffects.getEffectsList(): List<ActionEffect> {
    return getEffectsListByFace(SUCCESS) +
            getEffectsListByFace(BOON) +
            getEffectsListByFace(SIGMAR) +
            getEffectsListByFace(BANE) +
            getEffectsListByFace(FAILURE) +
            getEffectsListByFace(CHAOS) +
            getEffectsListByFace(EXHAUSTION)
}

fun ActionEffects.getEffectsListByFace(face: Face): Iterable<ActionEffect> {
    val list = this[face]?.map {
        ActionEffect(face, it.key, it.value)
    } ?: listOf()

    return list.sortedBy { it.faceCount }
}
