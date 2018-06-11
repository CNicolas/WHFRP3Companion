package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization

fun Player.addEffect(effect: Effect): Player {
    val mutableEffects = effects.toMutableList()

    val alreadyExistingEffect = mutableEffects.find { effect.name == name }
    if (alreadyExistingEffect == null) {
        mutableEffects.add(effect)
    }

    effects = mutableEffects.toList()

    return this
}

fun Player.removeEffect(effect: Effect): Player {
    val mutableEffects = effects.toMutableList()
    mutableEffects.remove(effect)
    effects = mutableEffects.toList()

    return this
}

fun Player.getEffectsApplyingTo(characteristic: Characteristic): List<Effect> =
        effects.filter { effect ->
            effect.trigger.let {
                it.everyThrow || it.everyCharacteristic || it.characteristics?.contains(characteristic) == true
            }
        }

fun Player.getEffectsApplyingTo(skill: Skill, strict: Boolean = false): List<Effect> {
    return if (strict) {
        effects.filter { effect ->
            effect.trigger.let {
                it.everySkill || it.skills?.contains(skill) == true
            }
        }
    } else {
        effects.filter { effect ->
            effect.trigger.let {
                it.everyThrow || it.everyCharacteristic || it.everySkill || it.skills?.contains(skill) == true
            }
        }
    }
}

fun Player.getEffectsApplyingTo(skill: Skill, specialization: Specialization, strict: Boolean = false): List<Effect> {
    return if (strict) {
        effects.filter { effect ->
            effect.trigger.let {
                it.specializations?.contains(specialization) == true
            }
        }
    } else {
        effects.filter { effect ->
            effect.trigger.let {
                it.everyThrow || it.everyCharacteristic || it.everySkill
                        || it.skills?.contains(skill) == true
                        || it.specializations?.contains(specialization) == true
            }
        }
    }
}


fun Player.getEffectsApplyingToItems(): List<Effect> = effects.filter { it.trigger.everyItem }
fun Player.getEffectsApplyingToWeapons(): List<Effect> = effects.filter { effect ->
    effect.trigger.let {
        it.everyItem || it.everyWeapon
    }
}
