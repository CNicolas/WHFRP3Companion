package com.nicolas.dicelauncher.launch

import com.nicolas.dicelauncher.dices.Face.*
import com.nicolas.whfrp3database.entities.hand.Hand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LaunchHelpersTest {
    @Test
    fun should_simplify_a_list_of_faces() {
        val faces = listOf(DELAY, SUCCESS, FAILURE, BOON, BANE, BOON)
        val finalFaces = simplifyFaces(faces)

        println(faces)
        println(finalFaces)

        assertThat(finalFaces).isNotEmpty
        assertThat(finalFaces.size).isEqualTo(2)
        assertThat(finalFaces).containsExactlyInAnyOrder(BOON, DELAY)
    }

    @Test
    fun should_launch_hand_and_be_successful() {
        val hand = Hand(name = "SampleHand", characteristicDicesCount = 10)
        val launchResult = hand.launch()

        assertThat(launchResult.isSuccessful).isTrue()

        println(launchResult)
    }

    @Test
    fun should_launch_hand_and_be_unsuccessful() {
        val hand = Hand(name = "SampleHand", challengeDicesCount = 10)
        val launchResult = launchHand(hand)

        assertThat(launchResult.isSuccessful).isFalse()

        println(launchResult)
    }
}