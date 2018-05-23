package com.nicolas.models.extensions

import com.nicolas.models.dice.DiceType.*
import com.nicolas.models.hand.DifficultyLevel
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization

fun Player.createHand(characteristic: Characteristic,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this[characteristic].getHand(name, difficultyLevel)

    effects.forEach { effect ->
        if (effect.characteristics?.contains(characteristic) == true) {
            hand.applyEffectDices(effect)
        }
    }

    return hand
}

fun Player.createHand(skill: Skill,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this.createHand(skill.characteristic, name, difficultyLevel)
    hand.expertiseDicesCount += skill.level

    effects.forEach { effect ->
        if (effect.allSkills || effect.skills?.contains(skill) == true) {
            hand.applyEffectDices(effect)
        }
    }

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

    effects.forEach { effect ->
        if (effect.specializations?.contains(specialization) == true) {
            hand.applyEffectDices(effect)
        }
    }

    return hand
}

private fun Hand.applyEffectDices(effect: Effect): Hand {
    effect.addedDices?.forEach {
        when (it.type) {
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
        when (it.type) {
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