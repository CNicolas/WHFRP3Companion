package com.nicolas.playersheet.player

import com.nicolas.models.extensions.getSkillByName
import com.nicolas.models.hand.DifficultyLevel.HARD
import com.nicolas.models.hand.DifficultyLevel.MEDIUM
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.playersheet.extensions.createHand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PlayerHandTest {

    @Test
    fun should_create_strength_hand_from_player() {
        val player = Player(name = "PlayerName", strength = CharacteristicValue(3, 1))

        val hand = player.createHand(Characteristic.STRENGTH, "Hand")
        assertThat(hand.name).isEqualTo("Hand")
        assertThat(hand.characteristicDicesCount).isEqualTo(3)
        assertThat(hand.fortuneDicesCount).isEqualTo(1)
    }

    @Test
    fun should_create_hand_from_skill() {
        val player = Player("John")
        player.strength = CharacteristicValue(4, 2)
        player.getSkillByName("capacité de combat")!!.level = 2
        val hand = player.createHand(player.getSkillByName("capacité de combat")!!, "HandName", HARD)

        assertThat(hand.name).isEqualTo("HandName")
        assertThat(hand.characteristicDicesCount).isEqualTo(4)
        assertThat(hand.fortuneDicesCount).isEqualTo(2)
        assertThat(hand.expertiseDicesCount).isEqualTo(2)
        assertThat(hand.challengeDicesCount).isEqualTo(HARD.ordinal)
    }

    @Test
    fun should_create_hand_from_specialized_skill() {
        val player = Player("John")
        player.strength = CharacteristicValue(5, 2)

        val fightSkill = player.getSkillByName("capacité de combat")!!
        fightSkill.level = 2
        fightSkill.specializations[0].isSpecialized = true

        val hand = player.createHand(fightSkill, fightSkill.specializations[0], "HandName", MEDIUM)

        assertThat(hand.name).isEqualTo("HandName")
        assertThat(hand.characteristicDicesCount).isEqualTo(5)
        assertThat(hand.fortuneDicesCount).isEqualTo(player.strength.fortuneValue + 1)
        assertThat(hand.expertiseDicesCount).isEqualTo(fightSkill.level)
        assertThat(hand.challengeDicesCount).isEqualTo(MEDIUM.challengeDicesCount)
    }
}