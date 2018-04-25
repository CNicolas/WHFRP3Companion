package com.nicolas.playersheet.extensions

import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.playersheet.enums.ExhaustionState.*
import com.nicolas.playersheet.enums.StressState.*
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class StressAndExhaustionTest {
    @Test
    fun should_take_or_lose_stress() {
        var player = Player(name = "PlayerName")

        assertThat(player.stress).isEqualTo(0)
        player = player.addStress(2)
        assertThat(player.stress).isEqualTo(2)

        player = player.removeStress(1)
        assertThat(player.stress).isEqualTo(1)

        player = player.removeStress(2)
        assertThat(player.stress).isEqualTo(0)
    }

    @Test
    fun should_have_good_stress_state() {
        val playerInComa = Player(name = "PlayerName", willpower = CharacteristicValue(4), stress = 8)
        val playerStressed = Player(name = "PlayerName", willpower = CharacteristicValue(4), stress = 5)
        val playerNotStressed = Player(name = "PlayerName", willpower = CharacteristicValue(4), stress = 1)

        assertThat(playerInComa.stressState()).isEqualTo(FAINTED)
        assertThat(playerStressed.stressState()).isEqualTo(STRESSED)
        assertThat(playerNotStressed.stressState()).isEqualTo(NOT_STRESSED)
    }

    @Test
    fun should_take_or_lose_exhaustion() {
        var player = Player(name = "PlayerName")

        assertThat(player.exhaustion).isEqualTo(0)
        player = player.addExhaustion(2)
        assertThat(player.exhaustion).isEqualTo(2)

        player = player.removeExhaustion(1)
        assertThat(player.exhaustion).isEqualTo(1)

        player = player.removeExhaustion(2)
        assertThat(player.exhaustion).isEqualTo(0)
    }

    @Test
    fun should_have_good_exhaustion_state() {
        val playerInComa = Player(name = "PlayerName", toughness = CharacteristicValue(4), exhaustion = 8)
        val playerExhausted = Player(name = "PlayerName", toughness = CharacteristicValue(4), exhaustion = 5)
        val playerNotExhausted = Player(name = "PlayerName", toughness = CharacteristicValue(4), exhaustion = 1)

        assertThat(playerInComa.exhaustionState()).isEqualTo(COMA)
        assertThat(playerExhausted.exhaustionState()).isEqualTo(EXHAUSTED)
        assertThat(playerNotExhausted.exhaustionState()).isEqualTo(NOT_EXHAUSTED)
    }
}