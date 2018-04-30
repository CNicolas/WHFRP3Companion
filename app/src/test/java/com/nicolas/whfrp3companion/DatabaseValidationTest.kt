package com.nicolas.whfrp3companion

import com.nicolas.models.player.Player
import com.nicolas.whfrp3database.anko.PlayerFacade
import com.nicolas.whfrp3database.anko.database
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DatabaseValidationTest {
    private lateinit var playerFacade: PlayerFacade

    @Before
    fun setUp() {
        playerFacade = PlayerFacade(RuntimeEnvironment.application)
    }

    @Test
    fun should_list_players() {
        playerFacade.add(Player("John"))

        val players = playerFacade.findAll()
        assertThat(players).isNotEmpty
        assertThat(players[0].name).isEqualTo("John")
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}