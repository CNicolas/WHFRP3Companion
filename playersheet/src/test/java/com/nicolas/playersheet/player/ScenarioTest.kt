package com.nicolas.playersheet.player

import com.nicolas.diceroller.roll.roll
import com.nicolas.diceroller.roll.rollForStatistics
import com.nicolas.playersheet.extensions.createHand
import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.HandFacade
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.database
import com.nicolas.whfrp3database.entities.hand.DifficultyLevel
import com.nicolas.whfrp3database.entities.hand.Hand
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.AGILITY
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.FELLOWSHIP
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Weapon
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.Talent
import com.nicolas.whfrp3database.extensions.addItem
import com.nicolas.whfrp3database.extensions.addTalent
import com.nicolas.whfrp3database.extensions.equipTalent
import com.nicolas.whfrp3database.staticData.loadTalents
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
class ScenarioTest {
    private lateinit var handFacade: HandFacade
    private lateinit var playerFacade: PlayerFacade

    private lateinit var allTalents: List<Talent>

    @Before
    fun setUp() {
        handFacade = HandFacade(RuntimeEnvironment.application)
        playerFacade = PlayerFacade(RuntimeEnvironment.application)

        allTalents = loadTalents(RuntimeEnvironment.application)
    }

    @Test
    fun should_modify_a_player_and_roll_a_hand() {
        val player = playerFacade.add(Player(name = "John"))
        assertThat(player).isNotNull()

        player.careerName = "Assassin"
        player.rank = 3
        player.strength = CharacteristicValue(3)
        player.toughness = CharacteristicValue(4)
        player.agility = CharacteristicValue(5, 2)
        player.intelligence = CharacteristicValue(3)
        player.willpower = CharacteristicValue(4)
        player.fellowship = CharacteristicValue(2)

        val updatedPlayer1 = playerFacade.update(player)
        assertThat(updatedPlayer1.name).isEqualTo("John")
        assertThat(updatedPlayer1.careerName).isEqualTo("Assassin")
        assertThat(updatedPlayer1.rank).isEqualTo(3)
        assertThat(updatedPlayer1.strength).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer1.toughness).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer1.agility).isEqualTo(CharacteristicValue(5, 2))
        assertThat(updatedPlayer1.intelligence).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer1.willpower).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer1.fellowship).isEqualTo(CharacteristicValue(2))
        assertThat(updatedPlayer1.maxEncumbrance).isEqualTo(20)
        assertThat(updatedPlayer1.maxStress).isEqualTo(8)
        assertThat(updatedPlayer1.encumbrance).isEqualTo(0)

        player.addItem(Weapon(name = "Arc sylvain",
                damage = 5,
                criticalLevel = 3,
                quality = Quality.SUPERIOR,
                encumbrance = 5,
                range = Range.LONG,
                isEquipped = true))

        val updatedPlayer2 = playerFacade.update(player)
        assertThat(updatedPlayer2.name).isEqualTo("John")
        assertThat(updatedPlayer2.careerName).isEqualTo("Assassin")
        assertThat(updatedPlayer2.rank).isEqualTo(3)
        assertThat(updatedPlayer2.strength).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer2.toughness).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer2.agility).isEqualTo(CharacteristicValue(5, 2))
        assertThat(updatedPlayer2.intelligence).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer2.willpower).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer2.fellowship).isEqualTo(CharacteristicValue(2))
        assertThat(updatedPlayer2.maxEncumbrance).isEqualTo(20)
        assertThat(updatedPlayer2.maxStress).isEqualTo(8)
        assertThat(updatedPlayer2.encumbrance).isEqualTo(5)

        player.agility = player.agility.copy(fortuneValue = player.agility.fortuneValue + 1)

        val updatedPlayer3 = playerFacade.update(player)
        assertThat(updatedPlayer3.name).isEqualTo("John")
        assertThat(updatedPlayer3.careerName).isEqualTo("Assassin")
        assertThat(updatedPlayer3.rank).isEqualTo(3)
        assertThat(updatedPlayer3.strength).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer3.toughness).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer3.agility).isEqualTo(CharacteristicValue(5, 3))
        assertThat(updatedPlayer3.intelligence).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer3.willpower).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer3.fellowship).isEqualTo(CharacteristicValue(2))
        assertThat(updatedPlayer3.maxEncumbrance).isEqualTo(20)
        assertThat(updatedPlayer3.maxStress).isEqualTo(8)
        assertThat(updatedPlayer3.encumbrance).isEqualTo(5)

        val initiative = player.createHand(AGILITY, "Initiative").roll()
        assertThat(initiative.isSuccessful).isTrue()
        val impossible = player.createHand(FELLOWSHIP, "Impossible", DifficultyLevel.GODLIKE)
                .rollForStatistics(50)
        assertThat(impossible.successfulPercentage).isLessThan(70.0)

        val talent = allTalents.first { it.name == "Ascétisme" }
        player.addTalent(talent)
        player.equipTalent(talent)
        val updatedPlayer4 = playerFacade.update(player)
        assertThat(updatedPlayer4.talents.size).isEqualTo(1)
        assertThat(updatedPlayer4.talents[0].isEquipped).isTrue()
    }

    @Test
    fun should_use_dice_roller_alone() {
        val hand = Hand("default", 4, 1, 1, challengeDicesCount = 1)
        assertThat(hand.rollForStatistics(100).successfulPercentage).isGreaterThanOrEqualTo(50.0)

        hand.name = "Good charac"
        handFacade.add(hand)

        hand.misfortuneDicesCount = 2
        assertThat(hand.rollForStatistics(100).successfulPercentage).isGreaterThanOrEqualTo(20.0)
        hand.name = "Harder"
        handFacade.add(hand)

        val goodCharacHand = handFacade.find("Good charac")
        assertThat(goodCharacHand).isNotNull()
        assertThat(goodCharacHand!!.characteristicDicesCount).isEqualTo(4)
        assertThat(goodCharacHand.expertiseDicesCount).isEqualTo(1)
        assertThat(goodCharacHand.fortuneDicesCount).isEqualTo(1)
        assertThat(goodCharacHand.challengeDicesCount).isEqualTo(1)
        assertThat(goodCharacHand.misfortuneDicesCount).isEqualTo(0)

        val harderHand = handFacade.find("Harder")
        assertThat(harderHand).isNotNull()
        assertThat(harderHand!!.characteristicDicesCount).isEqualTo(4)
        assertThat(harderHand.expertiseDicesCount).isEqualTo(1)
        assertThat(harderHand.fortuneDicesCount).isEqualTo(1)
        assertThat(harderHand.challengeDicesCount).isEqualTo(1)
        assertThat(harderHand.misfortuneDicesCount).isEqualTo(2)

        hand.expertiseDicesCount = 0

        val secondHand2 = handFacade.update(hand)
        assertThat(secondHand2.name).isEqualTo("Harder")
        assertThat(secondHand2.characteristicDicesCount).isEqualTo(4)
        assertThat(secondHand2.expertiseDicesCount).isEqualTo(0)
        assertThat(secondHand2.fortuneDicesCount).isEqualTo(1)
        assertThat(secondHand2.challengeDicesCount).isEqualTo(1)
        assertThat(secondHand2.misfortuneDicesCount).isEqualTo(2)
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}