package com.nicolas.whfrp3database.hand

import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.anko.AnkoHandRepository
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
class AnkoHandRepositoryTest {
    private lateinit var ankoHandRepository: AnkoHandRepository

    @Before
    fun setUp() {
        ankoHandRepository = AnkoHandRepository(RuntimeEnvironment.application)
    }

    @Test
    fun should_add_a_simple_hand() {
        val handName = "HandName"

        val hand = ankoHandRepository.add(Hand(handName))
        assertThat(hand.name).isEqualTo(handName)
    }

    @Test
    fun should_find_an_added_simple_hand() {
        val handName = "HandName"

        val hand = ankoHandRepository.add(Hand(handName))
        assertThat(hand.name).isEqualTo(handName)

        val foundHand = ankoHandRepository.find(handName)
        assertThat(foundHand).isNotNull()
        assertThat(foundHand!!.name).isEqualTo(handName)

        assertThat(hand).isEqualToComparingFieldByField(foundHand)
    }

    @Test
    fun should_update_an_added_simple_hand() {
        val handName = "HandName"

        val hand = ankoHandRepository.add(Hand(handName))
        assertThat(hand.name).isEqualTo(handName)

        hand.characteristicDicesCount = 3
        hand.challengeDicesCount = 1

        val updatedHand = ankoHandRepository.update(hand)
        assertThat(updatedHand.name).isEqualTo(handName)
        assertThat(updatedHand.characteristicDicesCount).isEqualTo(3)
        assertThat(updatedHand.challengeDicesCount).isEqualTo(1)
        assertThat(updatedHand).isEqualToComparingFieldByField(hand)
    }

    @Test
    fun should_update_name_of_hand() {
        val hand = ankoHandRepository.add(Hand("Hand1"))
        assertThat(hand.name).isEqualTo("Hand1")

        hand.name = "Hand2"

        val updatedHand = ankoHandRepository.update(hand)
        assertThat(updatedHand.name).isEqualTo("Hand2")
        assertThat(updatedHand).isEqualToComparingFieldByField(hand)

        val allHands = ankoHandRepository.findAll()
        assertThat(allHands.size).isEqualTo(1)
    }

    @Test
    fun should_delete_a_hand() {
        val handName = "HandName"

        val hand = ankoHandRepository.add(Hand(handName))
        assertThat(hand.name).isEqualTo(handName)

        ankoHandRepository.delete(hand)
        assertThat(ankoHandRepository.findAll()).isEmpty()
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}