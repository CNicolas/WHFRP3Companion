package com.nicolas.playersheet.extensions

import com.nicolas.whfrp3database.entities.hand.DifficultyLevel
import com.nicolas.whfrp3database.entities.hand.Hand
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Specialization
import com.nicolas.whfrp3database.extensions.getSpecializationByName

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