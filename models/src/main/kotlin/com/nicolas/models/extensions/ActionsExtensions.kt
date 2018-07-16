package com.nicolas.models.extensions

import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.dice.DiceType
import com.nicolas.models.hand.DifficultyLevel
import com.nicolas.models.hand.Hand
import com.nicolas.models.item.enums.WeaponCategory
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Stance
import com.nicolas.models.player.enums.Stance.*
import com.nicolas.models.skill.Skill

fun Player.createHandOfAction(action: Action): Hand? {
    return action.skill?.let { skillName ->
        getSkillByName(skillName)?.let { skill ->
            val actionSide = getSideOfAction(action)
            println(actionSide)

            actionSide?.let { side ->
                val hand = side.difficulty?.let {
                    val tmpHand = createHandWithDifficulty(action, skill, it)

                    tmpHand.applyActionWeaponCategorySpecializations(action, skill)
                } ?: {
                    createHand(skill, action.name, DifficultyLevel.EASY)
                }()

                println(hand)

                hand
            }
        }
    }
}

fun Action.getSideByStance(stance: Stance): ActionSide? = when (stance) {
    CONSERVATIVE -> conservativeSide
    RECKLESS -> recklessSide
    NEUTRAL -> null
}

private fun Player.getSideOfAction(action: Action): ActionSide? {
    return action.getSideByStance(currentStance) ?: action.getSideByStance(dominantStance)
}

private fun Player.createHandWithDifficulty(action: Action, skill: Skill, difficulty: List<DiceType>): Hand {
    return if (difficulty.isNotEmpty()) {
        val tmpHand = createHand(skill, action.name)

        difficulty.forEach {
            tmpHand[it] += 1
        }

        tmpHand
    } else {
        createHand(skill, action.name, DifficultyLevel.EASY)
    }
}


private fun Hand.applyActionWeaponCategorySpecializations(action: Action, skill: Skill): Hand {
    return action.conditions?.let { conditions ->
        val categories = conditions.mapNotNull { it.weapon }
                .mapNotNull { it.categories }
                .flatMap { it }

        applyWeaponTypeSpecialization(skill, categories)
    } ?: this
}

private fun Hand.applyWeaponTypeSpecialization(skill: Skill, weaponCategories: List<WeaponCategory>): Hand {
    skill.specializations
            .filter { it.isSpecialized }
            .forEach { specialization ->
                specialization.weaponTypes?.let { weaponTypes ->
                    val count = weaponTypes.count { weaponCategories.contains(it.category) }
                    fortuneDicesCount += count
                }
            }

    return this
}