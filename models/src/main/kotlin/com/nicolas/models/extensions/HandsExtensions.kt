package com.nicolas.models.extensions

import com.nicolas.models.dice.DiceType.*
import com.nicolas.models.effect.Effect
import com.nicolas.models.hand.DifficultyLevel
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization

fun Player.createHand(characteristic: Characteristic,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this[characteristic].getHand(name, difficultyLevel)

    when {
        stance < 0 -> {
            hand.characteristicDicesCount -= Math.abs(stance)
            hand.conservativeDicesCount += Math.abs(stance)
        }
        stance > 0 -> {
            hand.characteristicDicesCount -= Math.abs(stance)
            hand.recklessDicesCount += Math.abs(stance)
        }
    }

    getEffectsApplyingTo(characteristic)
            .forEach { hand.applyEffectDices(it) }

    return hand
}

fun Player.createHand(skill: Skill,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this.createHand(skill.characteristic, name, difficultyLevel)
    hand.expertiseDicesCount += skill.level

    getEffectsApplyingTo(skill, true)
            .forEach { hand.applyEffectDices(it) }

    return hand
}

fun Player.createHand(skill: Skill,
                      specialization: Specialization,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = createHand(skill, name, difficultyLevel)

    if (skill.getSpecializationByName(specialization.name)?.isSpecialized == true) {
        hand.fortuneDicesCount += 1
    }

    getEffectsApplyingTo(skill, specialization, true)
            .forEach { hand.applyEffectDices(it) }

    return hand
}

private fun Hand.applyEffectDices(effect: Effect): Hand {
    effect.addedDices?.forEach {
        when (it) {
            CHARACTERISTIC -> characteristicDicesCount += 1
            CONSERVATIVE -> conservativeDicesCount += 1
            RECKLESS -> recklessDicesCount += 1
            EXPERTISE -> expertiseDicesCount += 1
            FORTUNE -> fortuneDicesCount += 1
            CHALLENGE -> challengeDicesCount += 1
            MISFORTUNE -> misfortuneDicesCount += 1
        }
    }

    effect.removedDices?.forEach {
        when (it) {
            CHARACTERISTIC -> characteristicDicesCount -= 1
            CONSERVATIVE -> conservativeDicesCount -= 1
            RECKLESS -> recklessDicesCount -= 1
            EXPERTISE -> expertiseDicesCount -= 1
            FORTUNE -> fortuneDicesCount -= 1
            CHALLENGE -> challengeDicesCount -= 1
            MISFORTUNE -> misfortuneDicesCount -= 1
        }
    }

    return this
}