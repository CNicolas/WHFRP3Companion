package com.nicolas.models.actions

import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.Trait
import com.nicolas.models.action.condition.ActionCondition
import com.nicolas.models.action.condition.ActionConditionWeapon
import com.nicolas.models.action.effect.*
import com.nicolas.models.action.effect.Target
import com.nicolas.models.dice.Face.*
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.WeaponCategory
import com.nicolas.models.player.enums.Stance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SizeAndOperatorsTest {
    private val rangeAttackSides: ActionSide = ActionSide(
            stance = Stance.CONSERVATIVE,
            cooldown = 0,
            difficulty = listOf(),
            effects = hashMapOf(
                    SUCCESS to hashMapOf(1 to listOf(ActionFaceEffect(0)),
                            3 to listOf(ActionFaceEffect(2))),
                    BOON to hashMapOf(2 to listOf(ActionFaceEffect(maneuver = true))),
                    BANE to hashMapOf(2 to listOf(ActionFaceEffect(canEngage = Target.TARGET))))
    )
    private val rangeAttack: Action =
            Action(
                    name = "Tir",
                    type = ActionType.RANGE_ATTACK,
                    conservativeSide = rangeAttackSides,
                    recklessSide = rangeAttackSides,
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
    fun should_return_success_size() {
        val success = hashMapOf(1 to listOf(ActionFaceEffect()), 3 to listOf(ActionFaceEffect()), 4 to listOf(ActionFaceEffect()))

        assertThat(success.size).isEqualTo(3)
    }

    @Test
    fun should_return_effects_size() {
        val success: ActionFaceEffectByCount = hashMapOf(1 to listOf(ActionFaceEffect()), 3 to listOf(ActionFaceEffect()), 4 to listOf(ActionFaceEffect()))
        val bane: ActionFaceEffectByCount = hashMapOf(2 to listOf(ActionFaceEffect()))
        val effects: ActionEffects = hashMapOf(SUCCESS to success, BANE to bane)

        assertThat(success.size).isEqualTo(3)
        assertThat(bane.size).isEqualTo(1)
        assertThat(effects.size).isEqualTo(2)
        assertThat(effects.effectsCount).isEqualTo(4)
    }

    @Test
    fun should_return_effect_by_index() {
        val effects = rangeAttack.conservativeSide.effects!!

        assertThat(effects[-1]).isNull()
        assertThat(effects[0]).isNotNull
        assertThat(effects[0]!!.face).isEqualTo(SUCCESS)
        assertThat(effects[0]!!.effect.damage).isEqualTo(0)
        assertThat(effects[1]).isNotNull
        assertThat(effects[1]!!.face).isEqualTo(SUCCESS)
        assertThat(effects[1]!!.effect.damage).isEqualTo(2)
        assertThat(effects[2]).isNotNull
        assertThat(effects[2]!!.face).isEqualTo(BOON)
        assertThat(effects[2]!!.effect.maneuver).isTrue()
        assertThat(effects[3]).isNotNull
        assertThat(effects[3]!!.face).isEqualTo(BANE)
        assertThat(effects[3]!!.effect.canEngage).isEqualTo(Target.TARGET)
        assertThat(effects[4]).isNull()

        assertThat(effects.effectsCount).isEqualTo(4)
    }

    @Test
    fun should_return_string_of_ActionFaceEffect() {
        val successEffect = ActionFaceEffect(damage = 0, ignoreSoak = true)
        val sigmarEffect = ActionFaceEffect(damage = 2, critical = 1)

        assertThat(successEffect.toString()).isEqualTo("Vous infligez les dégâts normaux, ignorez l'encaissement")
        assertThat(sigmarEffect.toString()).isEqualTo("Vous infligez les dégâts +2, +1 critique")
    }

    @Test
    fun should_toString_of_WeaponCondition() {
        val conditionRange = ActionConditionWeapon(categories = listOf(WeaponCategory.RANGE))
        assertThat(conditionRange.toString()).isEqualTo("Arme à distance")

        val conditionTwoMeleeHand = ActionConditionWeapon(categories = listOf(WeaponCategory.MELEE), twoHanded = true, inHand = true)
        assertThat(conditionTwoMeleeHand.toString()).isEqualTo("Arme de mêlée à deux mains en main")

        assertThat(rangeAttack.conditions?.get(0).toString()).isEqualTo("Arme à distance, arme à feu, arme à répétition équipée")
        assertThat(rangeAttack.conditions?.get(1).toString()).isEqualTo("Désengagé")
    }

    @Test
    fun should_toString_of_Condition() {
        assertThat(rangeAttack.conditionsString).isEqualTo("Arme à distance, arme à feu, arme à répétition équipée. Désengagé")
    }

    @Test
    fun should() {
        val effects: ActionEffects = hashMapOf(
                SUCCESS to hashMapOf(
                        2 to listOf(
                                ActionFaceEffect(1),
                                ActionFaceEffect(critical = 1)
                        )
                )
        )

        val effectsList = effects.getEffectsListByFace(SUCCESS)
        println(effectsList)

        assertThat(effectsList.size).isEqualTo(2)
        assertThat(effectsList[0].effect.damage).isEqualTo(1)
        assertThat(effectsList[0].effect.critical).isNull()
        assertThat(effectsList[1].effect.damage).isNull()
        assertThat(effectsList[1].effect.critical).isEqualTo(1)

    }
}