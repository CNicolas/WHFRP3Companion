package com.nicolas.diceroller.roll

import com.nicolas.models.dice.Face.*
import com.nicolas.models.hand.Hand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RollHelpersTest {
    @Test
    fun should_simplify_a_list_of_faces() {
        val faces = listOf(DELAY, SUCCESS, FAILURE, BOON, BANE, BOON)
        val finalFaces = simplifyFaces(faces)

        assertThat(finalFaces).isNotEmpty
        assertThat(finalFaces.size).isEqualTo(2)
        assertThat(finalFaces).containsExactlyInAnyOrder(BOON, DELAY)
    }

    @Test
    fun should_roll_hand_and_be_successful() {
        val hand = Hand(name = "SampleHand", characteristicDicesCount = 10)
        val rollResult = hand.roll()

        assertThat(rollResult.isSuccessful).isTrue()
    }

    @Test
    fun should_roll_hand_and_be_unsuccessful() {
        val hand = Hand(name = "SampleHand", challengeDicesCount = 10)
        val rollResult = rollHand(hand)

        assertThat(rollResult.isSuccessful).isFalse()
    }
}