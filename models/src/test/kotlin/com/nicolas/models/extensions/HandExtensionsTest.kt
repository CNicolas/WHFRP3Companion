package com.nicolas.models.extensions

import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.enums.Stance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HandExtensionsTest {

    @Test
    fun should_have_1_conservative_dice() {
        val player = Player("PlayerName", strength = CharacteristicValue(1), maxConservative = 1, stance = -1)
        assertThat(player.currentStance).isEqualTo(Stance.CONSERVATIVE)

        val hand = player.createHand(Characteristic.STRENGTH)
        assertThat(hand.conservativeDicesCount).isEqualTo(1)
        assertThat(hand.characteristicDicesCount).isEqualTo(0)
    }

    @Test
    fun should_have_1_reckless_dice_after_applying_stance_several_times() {
        val player = Player("PlayerName", strength = CharacteristicValue(2), maxConservative = 1, maxReckless = 2, stance = 0)
        assertThat(player.currentStance).isEqualTo(Stance.NEUTRAL)
        assertThat(player.dominantStance).isEqualTo(Stance.RECKLESS)

        var hand = player.createHand(Characteristic.STRENGTH)
        assertThat(hand.characteristicDicesCount).isEqualTo(2)
        assertThat(hand.conservativeDicesCount).isEqualTo(0)
        assertThat(hand.recklessDicesCount).isEqualTo(0)

        player.stance = 1
        hand = hand.applyStanceDices(player.stance)
        assertThat(hand.characteristicDicesCount).isEqualTo(1)
        assertThat(hand.conservativeDicesCount).isEqualTo(0)
        assertThat(hand.recklessDicesCount).isEqualTo(1)

        player.stance = -1
        hand = hand.applyStanceDices(player.stance)
        assertThat(hand.characteristicDicesCount).isEqualTo(1)
        assertThat(hand.conservativeDicesCount).isEqualTo(1)
        assertThat(hand.recklessDicesCount).isEqualTo(0)

        player.stance = 2
        hand = hand.applyStanceDices(player.stance)
        assertThat(hand.characteristicDicesCount).isEqualTo(0)
        assertThat(hand.conservativeDicesCount).isEqualTo(0)
        assertThat(hand.recklessDicesCount).isEqualTo(2)
    }
}