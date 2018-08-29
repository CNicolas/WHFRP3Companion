package com.nicolas.diceroller.hand

import com.nicolas.diceroller.roll.rollHand
import com.nicolas.models.hand.Hand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HandTest {
    @Test
    fun should_roll_simple_hand() {
        val hand = Hand(name = "SampleHand", characteristicDicesCount = 2, fortuneDicesCount = 1)
        val roll = rollHand(hand)

        assertThat(roll.faces.size).isEqualTo(3)
    }

    @Test
    fun should_roll_rank_3_hand() {
        val hand = Hand(name = "SampleHand",
                fortuneDicesCount = 2,
                characteristicDicesCount = 5,
                expertiseDicesCount = 2,
                challengeDicesCount = 1,
                misfortuneDicesCount = 2)
    }
}