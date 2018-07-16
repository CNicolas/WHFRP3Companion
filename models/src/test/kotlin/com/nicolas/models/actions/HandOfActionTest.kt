package com.nicolas.models.actions

import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.Trait
import com.nicolas.models.action.condition.ActionCondition
import com.nicolas.models.action.condition.ActionConditionWeapon
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.action.effect.Target
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.Face
import com.nicolas.models.extensions.createHandOfAction
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.WeaponCategory
import com.nicolas.models.item.enums.WeaponType
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.enums.Stance
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.SkillType
import com.nicolas.models.skill.Specialization
import org.assertj.core.api.SoftAssertions
import org.junit.Test

class HandOfActionTest {
    private val shootingSkill = Skill("Capacité de Tir",
            Characteristic.AGILITY,
            SkillType.BASIC,
            1,
            specializations = listOf(Specialization("Arcs", true, listOfNotNull(WeaponType.BOW)))
    )
    private val player = Player("PlayerName",
            agility = CharacteristicValue(3, 1),
            maxConservative = 3,
            maxReckless = 1,
            skills = listOf(shootingSkill)
    )

    private val rangeAttack: Action =
            Action(
                    name = "Tir",
                    type = ActionType.RANGE_ATTACK,
                    conservativeSide = ActionSide(
                            stance = Stance.CONSERVATIVE,
                            cooldown = 0,
                            difficulty = listOf(DiceType.MISFORTUNE),
                            effects = hashMapOf(
                                    Face.SUCCESS to hashMapOf(1 to ActionFaceEffect(0),
                                            3 to ActionFaceEffect(2)),
                                    Face.BOON to hashMapOf(2 to ActionFaceEffect(maneuver = true)),
                                    Face.BANE to hashMapOf(2 to ActionFaceEffect(canEngage = Target.TARGET)))
                    ),
                    recklessSide = ActionSide(
                            stance = Stance.RECKLESS,
                            cooldown = 0,
                            difficulty = listOf(),
                            effects = hashMapOf(
                                    Face.SUCCESS to hashMapOf(1 to ActionFaceEffect(0),
                                            3 to ActionFaceEffect(2)),
                                    Face.BOON to hashMapOf(2 to ActionFaceEffect(maneuver = true)),
                                    Face.BANE to hashMapOf(2 to ActionFaceEffect(canEngage = Target.TARGET)))
                    ),
                    traits = listOf(Trait.BASIC),
                    conditions = listOf(
                            ActionCondition(
                                    ActionConditionWeapon(
                                            listOf(WeaponCategory.RANGE, WeaponCategory.FIRE_ARM, WeaponCategory.REPEATING),
                                            equipped = true
                                    )
                            ),
                            ActionCondition(
                                    range = Range.ENGAGED,
                                    target = Target.NONE
                            )
                    ),
                    skill = "Capacité de Tir",
                    targetDefense = true
            )

    @Test
    fun should_have_the_skill_dices_neutral() {
        val hand = player.createHandOfAction(rangeAttack)

        val softly = SoftAssertions()
        softly.apply {
            assertThat(hand).`as`("Not null")
                    .isNotNull
            assertThat(hand!!.name).`as`("Name")
                    .isEqualTo("Tir")
            assertThat(hand.characteristicDicesCount).`as`("Characteristics")
                    .isEqualTo(3)
            assertThat(hand.expertiseDicesCount).`as`("Expertise")
                    .isEqualTo(1)
            assertThat(hand.fortuneDicesCount).`as`("Fortune")
                    .isEqualTo(2)
            assertThat(hand.conservativeDicesCount).`as`("Conservative")
                    .isEqualTo(0)
            assertThat(hand.recklessDicesCount).`as`("Reckless")
                    .isEqualTo(0)
            assertThat(hand.challengeDicesCount).`as`("Challenge")
                    .isEqualTo(0)
            assertThat(hand.misfortuneDicesCount).`as`("Misfortune")
                    .isEqualTo(1)
        }
        softly.assertAll()
    }

    @Test
    fun should_have_the_skill_dices_reckless() {
        player.stance = 1
        val hand = player.createHandOfAction(rangeAttack)

        val softly = SoftAssertions()
        softly.apply {
            assertThat(hand).`as`("Not null")
                    .isNotNull
            assertThat(hand!!.name).`as`("Name")
                    .isEqualTo("Tir")
            assertThat(hand.characteristicDicesCount).`as`("Characteristics")
                    .isEqualTo(2)
            assertThat(hand.expertiseDicesCount).`as`("Expertise")
                    .isEqualTo(1)
            assertThat(hand.fortuneDicesCount).`as`("Fortune")
                    .isEqualTo(2)
            assertThat(hand.conservativeDicesCount).`as`("Conservative")
                    .isEqualTo(0)
            assertThat(hand.recklessDicesCount).`as`("Reckless")
                    .isEqualTo(1)
            assertThat(hand.challengeDicesCount).`as`("Challenge")
                    .isEqualTo(1)
            assertThat(hand.misfortuneDicesCount).`as`("Misfortune")
                    .isEqualTo(0)
        }
        softly.assertAll()
    }
}