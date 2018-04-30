package com.nicolas.whfrp3companion

import com.nicolas.models.player.Player
import com.nicolas.whfrp3database.anko.AnkoPlayerRepository
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
    private lateinit var ankoPlayerRepository: AnkoPlayerRepository

    @Before
    fun setUp() {
        ankoPlayerRepository = AnkoPlayerRepository(RuntimeEnvironment.application)
    }

    @Test
    fun should_list_players() {
        ankoPlayerRepository.add(Player("John"))

        val players = ankoPlayerRepository.findAll()
        assertThat(players).isNotEmpty
        assertThat(players[0].name).isEqualTo("John")
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}