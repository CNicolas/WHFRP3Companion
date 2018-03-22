package com.nicolas.playersheet.extensions

import com.nicolas.playersheet.enums.PlayerLivingState
import com.nicolas.whfrp3database.entities.hand.DifficultyLevel
import com.nicolas.whfrp3database.entities.hand.DifficultyLevel.NONE
import com.nicolas.whfrp3database.entities.hand.Hand
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Specialization
import com.nicolas.whfrp3database.extensions.getSpecializationByName

fun Player.createHand(characteristic: Characteristic,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = NONE): Hand =
        this[characteristic].getHand(name, difficultyLevel)

fun Player.createHand(skill: Skill,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = NONE): Hand {
    val hand = this.createHand(skill.characteristic, name, difficultyLevel)
    hand.expertiseDicesCount += skill.level

    return hand
}

fun Player.createHand(skill: Skill,
                      specialization: Specialization,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = NONE): Hand {
    val hand = createHand(skill, name, difficultyLevel)

    if(skill.getSpecializationByName(specialization.name)?.isSpecialized!!) {
        hand.fortuneDicesCount += 1
    }

    return hand
}

fun Player.earnExperiencePoints(experiencePoints: Int): Player {
    experience += experiencePoints
    maxExperience += experiencePoints

    return this
}

val Player.livingState: PlayerLivingState
    get() {
        val woundsBeforeDying = maxWounds - wounds
        return when {
            woundsBeforeDying < 0 -> PlayerLivingState.DEAD
            woundsBeforeDying == 0 -> PlayerLivingState.KO
            woundsBeforeDying <= maxWounds / 2 -> PlayerLivingState.HEAVILY_INJURED
            woundsBeforeDying < maxWounds -> PlayerLivingState.SLIGHTLY_INJURED
            else -> PlayerLivingState.UNINJURED
        }
    }

fun Player.receiveDamage(damage: Int): Pair<Int, PlayerLivingState> {
    val maximumDamage = damage - soak - toughness.value
    val damageDealt = when {
        maximumDamage <= 0 -> 1
        else -> maximumDamage
    }

    return damageDealt to loseHealth(damageDealt)
}

fun Player.loseHealth(damage: Int): PlayerLivingState {
    wounds += damage
    return livingState
}