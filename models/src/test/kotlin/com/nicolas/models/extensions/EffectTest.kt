package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import com.nicolas.models.player.effect.EffectDuration
import com.nicolas.models.player.effect.Trigger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EffectTest {
    @Test
    fun should_have_one_soak() {
        val player = Player(name = "PlayerName",
                effects = listOf(Effect("Soaking",
                        soak = 1,
                        trigger = Trigger(always = true),
                        duration = EffectDuration.BRIEF)))

        assertThat(player.soak).isEqualTo(1)
    }

    @Test
    fun should_add_an_effect() {
        val player = Player("PlayerName")

        assertThat(player.effects).isEmpty()
        player.addEffect(Effect("Stressed", stress = 3))
        player.addEffect(Effect("Cooldowned", cooldown = 1))
        assertThat(player.effects.size).isEqualTo(2)
        assertThat(player.effects.map { it.name }).containsExactly("Stressed", "Cooldowned")
        player.removeEffect(Effect("Stressed", stress = 3))
        player.addEffect(Effect("OK", exhaustion = 1))
        assertThat(player.effects.size).isEqualTo(2)
        assertThat(player.effects.map { it.name }).containsExactly("Cooldowned", "OK")
    }
}