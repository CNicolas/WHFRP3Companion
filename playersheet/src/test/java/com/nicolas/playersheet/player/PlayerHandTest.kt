package com.nicolas.playersheet.player

import com.nicolas.playersheet.extensions.createHand
import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.database
import com.nicolas.whfrp3database.entities.hand.DifficultyLevel.HARD
import com.nicolas.whfrp3database.entities.hand.DifficultyLevel.MEDIUM
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.extensions.getSkillByName
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
class PlayerHandTest {
    private lateinit var playerFacade: PlayerFacade

    @Before
    fun setUp() {
        playerFacade = PlayerFacade(RuntimeEnvironment.application)
    }

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
        val player = playerFacade.add(Player("John"))
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
        val player = playerFacade.add(Player("John"))
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

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}