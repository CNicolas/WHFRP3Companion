package com.nicolas.whfrp3database

import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.GenericItem
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
class PlayerFacadeTest {
    private lateinit var playerFacade: PlayerFacade

    @Before
    fun setUp() {
        playerFacade = PlayerFacade(RuntimeEnvironment.application)
    }

    @Test
    fun should_add_a_simple_player() {
        val playerName = "PlayerName"

        val player = playerFacade.add(Player(playerName))
        assertThat(player.name).isEqualTo(playerName)
    }

    @Test
    fun should_find_an_added_simple_player() {
        val playerName = "PlayerName"

        val player = playerFacade.add(Player(playerName))
        assertThat(player.name).isEqualTo(playerName)

        val foundPlayer = playerFacade.find(playerName)
        assertThat(foundPlayer).isNotNull()
        assertThat(foundPlayer!!.name).isEqualTo(playerName)

        assertThat(player).isEqualToComparingFieldByField(foundPlayer)
    }

    @Test
    fun should_update_an_added_simple_player() {
        val playerName = "PlayerName"

        val player = playerFacade.add(Player(playerName))
        assertThat(player.name).isEqualTo(playerName)

        player.careerName = "Soldier"
        player.toughness = CharacteristicValue(4, 1)

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.name).isEqualTo(playerName)
        assertThat(updatedPlayer.careerName).isEqualTo("Soldier")
        assertThat(updatedPlayer.toughness.value).isEqualTo(4)
        assertThat(updatedPlayer.toughness.fortuneValue).isEqualTo(1)
        assertThat(updatedPlayer).isEqualToComparingFieldByField(player)
    }

    @Test
    fun should_update_name_of_player() {
        val player = playerFacade.add(Player("John", items = listOf(GenericItem(name = "Rope"))))
        assertThat(player.name).isEqualTo("John")
        assertThat(player.items.size).isEqualTo(1)

        player.name = "Jack"

        val updatedPlayer = playerFacade.update(player)
        assertThat(player.name).isEqualTo("Jack")
        assertThat(player.items.size).isEqualTo(1)

        assertThat(updatedPlayer.name).isEqualTo("Jack")
        assertThat(updatedPlayer).isEqualToComparingFieldByField(player)
        assertThat(updatedPlayer.items.size).isEqualTo(1)

        val allPlayers = playerFacade.findAll()
        assertThat(allPlayers.size).isEqualTo(1)
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}