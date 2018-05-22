package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EffectTest {
    @Test
    fun should_have_one_soak() {
        val player = Player(name = "PlayerName", effects = listOf(Effect("Soaking", soak = 1)))

        assertThat(player.soak).isEqualTo(1)
    }
}