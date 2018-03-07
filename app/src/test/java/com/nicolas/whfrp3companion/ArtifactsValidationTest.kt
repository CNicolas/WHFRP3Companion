package com.nicolas.whfrp3companion

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Race.WOOD_ELF

class ArtifactsValidationTest {
    @Test
    fun should_save_player() {
        val player = WarHammerContext.playerFacade.save(Player("Jimmy", race = WOOD_ELF))

        assertThat(player.race).isEqualTo(WOOD_ELF)
        assertThat(WarHammerContext.playerFacade.findAll()).isNotEmpty
    }
}