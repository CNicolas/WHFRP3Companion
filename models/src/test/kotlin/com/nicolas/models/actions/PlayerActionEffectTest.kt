package com.nicolas.models.actions

import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.dice.Face
import com.nicolas.models.extensions.getActionEffectCritical
import com.nicolas.models.extensions.getActionEffectDamage
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.enums.Stance
import com.nicolas.models.skill.Skill
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PlayerActionEffectTest {
    private val player = Player("PlayerName",
            strength = CharacteristicValue(5),
            skills = listOf(Skill("Capacité de Combat", Characteristic.STRENGTH, level = 2))
    )
    private val weapon = Weapon(damage = 5, criticalLevel = 3)
    private val actionEffect = ActionFaceEffect()
    private val actionEffect2 = ActionFaceEffect(damage = 2)
    private val actionEffect3 = ActionFaceEffect(damage = 3)
    private val actionEffectCritical = ActionFaceEffect(critical = 1)
    private val action = Action("ActionName",
            ActionType.MELEE_ATTACK,
            listOf(),
            skill = "Capacité de Combat",
            conservativeSide = ActionSide(Stance.CONSERVATIVE,
                    effects = hashMapOf(Face.SUCCESS to hashMapOf(
                            1 to listOf(actionEffect),
                            2 to listOf(actionEffect2)))
            ),
            recklessSide = ActionSide(Stance.RECKLESS,
                    effects = hashMapOf(Face.SUCCESS to hashMapOf(
                            1 to listOf(actionEffect),
                            2 to listOf(actionEffect3)))
            )
    )

    @Test
    fun should_return_damage_without_weapon() {
        val damage = player.getActionEffectDamage(action.skill!!, actionEffect, null)

        assertThat(damage).isEqualTo(5)
    }

    @Test
    fun should_return_damages_with_weapon() {
        val damage10 = player.getActionEffectDamage(action.skill!!, actionEffect, weapon)
        val damage12 = player.getActionEffectDamage(action.skill!!, actionEffect2, weapon)
        val damage13 = player.getActionEffectDamage(action.skill!!, actionEffect3, weapon)
        val damage15 = player.getActionEffectDamage(action.skill!!, actionEffect + actionEffect2 + actionEffect3, weapon)

        assertThat(damage10).isEqualTo(10)
        assertThat(damage12).isEqualTo(12)
        assertThat(damage13).isEqualTo(13)
        assertThat(damage15).isEqualTo(15)
    }

    @Test
    fun should_return_critical_without_weapon() {
        val noCritical = getActionEffectCritical(actionEffect)
        val critical = getActionEffectCritical(actionEffectCritical)
        val criticalWithBoons = getActionEffectCritical(actionEffectCritical, 8)

        assertThat(noCritical).isEqualTo(0)
        assertThat(critical).isEqualTo(1)
        assertThat(criticalWithBoons).isEqualTo(1)
    }

    @Test
    fun should_return_critical_with_weapon() {
        val noCritical = getActionEffectCritical(actionEffect, weapon = weapon)
        val critical = getActionEffectCritical(actionEffectCritical, weapon = weapon)
        val noCriticalWithBoons = getActionEffectCritical(actionEffectCritical, 2, weapon)
        val criticalWithBoons = getActionEffectCritical(actionEffectCritical, 3, weapon)

        assertThat(noCritical).isEqualTo(0)
        assertThat(critical).isEqualTo(1)
        assertThat(noCriticalWithBoons).isEqualTo(1)
        assertThat(criticalWithBoons).isEqualTo(2)
    }
}