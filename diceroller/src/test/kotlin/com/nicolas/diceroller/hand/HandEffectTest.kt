package com.nicolas.diceroller.hand

import com.nicolas.models.dice.DiceType.*
import com.nicolas.models.effect.Effect
import com.nicolas.models.effect.Trigger
import com.nicolas.models.extensions.createHand
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HandEffectTest {

    @Test
    fun should_have_more_dices_on_skills() {
        val skills = listOf(Skill("Fight", Characteristic.STRENGTH),
                Skill("Distance", Characteristic.AGILITY))

        val player = Player(name = "PlayerName",
                strength = CharacteristicValue(1, 0),
                agility = CharacteristicValue(4, 1),
                skills = skills,
                effects = listOf(
                        Effect("Inspiré",
                                trigger = Trigger(everyRoll = true),
                                addedDices = listOf(FORTUNE)),
                        Effect("Borgne",
                                trigger = Trigger(skills = listOf(Skill("Distance", Characteristic.AGILITY))),
                                addedDices = listOf(CHALLENGE)),
                        Effect("Agile",
                                trigger = Trigger(characteristics = listOf(Characteristic.AGILITY)),
                                addedDices = listOf(EXPERTISE))
                )
        )

        val fightHand = player.createHand(skills[0])

        assertThat(fightHand.characteristicDicesCount).isEqualTo(1)
        assertThat(fightHand.fortuneDicesCount).isEqualTo(1)
        assertThat(fightHand.challengeDicesCount).isEqualTo(0)
        assertThat(fightHand.expertiseDicesCount).isEqualTo(0)

        val distanceHand = player.createHand(skills[1])

        assertThat(distanceHand.characteristicDicesCount).isEqualTo(4)
        assertThat(distanceHand.fortuneDicesCount).isEqualTo(2)
        assertThat(distanceHand.challengeDicesCount).isEqualTo(1)
        assertThat(distanceHand.expertiseDicesCount).isEqualTo(1)
    }
}