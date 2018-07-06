package com.nicolas.diceroller.statistics

import com.nicolas.diceroller.roll.rollForStatistics
import com.nicolas.diceroller.roll.rollHandForStatistics
import com.nicolas.models.hand.Hand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RollStatisticsTest {
    @Test
    fun should_calculate_total_report_of_expertise_dices() {
        val rollCount = 10
        val hand = Hand(name = "SampleHand", expertiseDicesCount = 5)
        val statistics = rollHandForStatistics(hand, rollCount)

        assertThat(statistics.rollCount).isEqualTo(rollCount)
        assertThat(statistics.successfulRollCount).isLessThanOrEqualTo(rollCount)
        assertThat(statistics.totalFailure).isZero()
        assertThat(statistics.averageSuccess).isGreaterThanOrEqualTo(1.0)
    }

    @Test
    fun should_calculate_statistics() {
        val rollCount = 100
        val hand = Hand(name = "SampleHand", characteristicDicesCount = 3, fortuneDicesCount = 1, challengeDicesCount = 2, misfortuneDicesCount = 1)

        val statistics = hand.rollForStatistics(rollCount)

        assertThat(statistics.rollCount).isEqualTo(rollCount)
        assertThat(statistics.successfulRollCount).isLessThanOrEqualTo(rollCount)
        assertThat(statistics.totalSigmar).isZero()
        assertThat(statistics.averageSuccess).isLessThanOrEqualTo(1.0)
    }

    @Test
    fun should_get_coherent_average_result() {
        val rollCount = 1000
        val hand = Hand(name = "SampleHand", characteristicDicesCount = 3, fortuneDicesCount = 1, challengeDicesCount = 1)
        val statistics = rollHandForStatistics(hand, rollCount)

        assertThat(statistics.rollCount).isEqualTo(rollCount)
        assertThat(statistics.successfulRollCount).isLessThanOrEqualTo(rollCount)
        assertThat(statistics.successfulPercentage).isBetween(10.0, 100.0)
    }
}