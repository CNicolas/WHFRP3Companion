package com.nicolas.diceroller.hand

import com.nicolas.models.dice.impl.bad.ChallengeDice
import com.nicolas.models.dice.impl.good.ExpertiseDice
import com.nicolas.models.dice.impl.good.FortuneDice
import com.nicolas.models.extensions.createHand
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
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
                                allSkills = true,
                                addedDices = listOf(FortuneDice())),
                        Effect("Borgne",
                                skills = listOf(Skill("Distance", Characteristic.AGILITY)),
                                addedDices = listOf(ChallengeDice())),
                        Effect("Agile",
                                characteristics = listOf(Characteristic.AGILITY),
                                addedDices = listOf(ExpertiseDice()))
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