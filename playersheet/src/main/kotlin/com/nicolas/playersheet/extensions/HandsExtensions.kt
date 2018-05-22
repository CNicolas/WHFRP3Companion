package com.nicolas.playersheet.extensions

import com.nicolas.models.extensions.getSpecializationByName
import com.nicolas.models.hand.DifficultyLevel
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization

fun Player.createHand(characteristic: Characteristic,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand =
        this[characteristic].getHand(name, difficultyLevel)

fun Player.createHand(skill: Skill,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this.createHand(skill.characteristic, name, difficultyLevel)
    hand.expertiseDicesCount += skill.level

    return hand
}

fun Player.createHand(skill: Skill,
                      specialization: Specialization,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = createHand(skill, name, difficultyLevel)

    if (skill.getSpecializationByName(specialization.name)?.isSpecialized!!) {
        hand.fortuneDicesCount += 1
    }

    return hand
}