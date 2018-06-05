package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.item.Armor
import com.nicolas.models.player.item.Weapon
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization

fun Player.getEffectsApplyingTo(characteristic: Characteristic): List<Effect> =
        effects.filter { it.allThrows || it.allCharacteristics || it.characteristics?.contains(characteristic) == true }

fun Player.getEffectsApplyingTo(skill: Skill, strict: Boolean = false): List<Effect> {
    return if (strict) {
        effects.filter { it.allSkills || it.skills?.contains(skill) == true }
    } else {
        effects.filter { it.allThrows || it.allCharacteristics || it.allSkills || it.skills?.contains(skill) == true }
    }
}

fun Player.getEffectsApplyingTo(skill: Skill, specialization: Specialization, strict: Boolean = false): List<Effect> {
    return if (strict) {
        effects.filter { it.specializations?.contains(specialization) == true }
    } else {
        effects.filter {
            it.allThrows || it.allCharacteristics || it.allSkills
                    || it.skills?.contains(skill) == true
                    || it.specializations?.contains(specialization) == true
        }
    }
}


fun Player.getEffectsApplyingToItems(): List<Effect> = effects.filter { it.allItems }
fun Player.getEffectsApplyingToArmors(): List<Effect> = effects.filter { it.allItems || it.allArmors }
fun Player.getEffectsApplyingToWeapons(): List<Effect> = effects.filter { it.allItems || it.allWeapons }

fun Player.getEffectApplyingTo(armor: Armor): List<Effect> =
        effects.filter {
            it.allItems || it.allArmors
                    || it.armorCategories?.contains(armor.category) == true
                    || it.armorTypes?.contains(armor.subType) == true
        }

fun Player.getEffectApplyingTo(weapon: Weapon): List<Effect> =
        effects.filter {
            it.allItems || it.allWeapons
                    || it.weaponCategories?.contains(weapon.category) == true
                    || it.weaponTypes?.contains(weapon.subType) == true
        }
