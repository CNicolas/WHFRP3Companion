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
}