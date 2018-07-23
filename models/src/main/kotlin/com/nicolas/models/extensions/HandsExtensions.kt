package com.nicolas.models.extensions

import com.nicolas.models.dice.DiceType.*
import com.nicolas.models.effect.Effect
import com.nicolas.models.hand.DifficultyLevel
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization
import java.lang.Math.abs

fun Player.createHand(characteristic: Characteristic,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this[characteristic].getHand(name, difficultyLevel)
            .applyStanceDices(stance)

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

fun Hand.applyStanceDices(stance: Int): Hand {
    val hand = copy()
    hand.characteristicDicesCount = hand.characteristicDicesCount + hand.conservativeDicesCount + hand.recklessDicesCount
    hand.conservativeDicesCount = 0
    hand.recklessDicesCount = 0

    if (hand.characteristicDicesCount > 0) {
        when {
            stance < 0 -> {
                val diff = hand.characteristicDicesCount - abs(stance)
                when {
                    diff < 0 -> {
                        hand.characteristicDicesCount = 0
                        hand.conservativeDicesCount += abs(stance) - abs(diff)
                    }
                    else -> {
                        hand.characteristicDicesCount -= abs(stance)
                        hand.conservativeDicesCount += abs(stance)
                    }
                }
            }
            stance > 0 -> {
                val diff = hand.characteristicDicesCount - abs(stance)
                when {
                    diff < 0 -> {
                        hand.characteristicDicesCount = 0
                        hand.recklessDicesCount += abs(stance) - abs(diff)
                    }
                    else -> {
                        hand.characteristicDicesCount -= abs(stance)
                        hand.recklessDicesCount += abs(stance)
                    }
                }
            }
        }
    }

    return hand
}