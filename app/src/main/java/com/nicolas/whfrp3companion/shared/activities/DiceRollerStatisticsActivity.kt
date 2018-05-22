package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nicolas.diceroller.roll.rollForStatistics
import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.HAND_ROLL_COUNT_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.content_dice_roller_statistics.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DiceRollerStatisticsActivity : AppCompatActivity() {
    private lateinit var hand: Hand
    private var rollCount: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roller_statistics)

        swipeRefreshLayout.setOnRefreshListener { calculateStatistics() }

        if (intent?.extras !== null) {
            hand = intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
            rollCount = intent.extras.getInt(HAND_ROLL_COUNT_INTENT_ARGUMENT)
        } else {
            hand = Hand("")
        }

        setSupportActionBar(toolbar)

        calculateStatistics()
    }

    private fun calculateStatistics() {
        swipeRefreshLayout.isRefreshing = true

        doAsync {
            val statistics = hand.rollForStatistics(rollCount)
            uiThread {
                totalRollCountView.text = getString(R.string.rolls_count_format).format(statistics.rollCount)
                successfulRollsView.text = getString(R.string.successful_rolls_with_percentage_format)
                        .format(statistics.successfulRollCount, statistics.successfulPercentage.formatWithDigits(2))

                averageSuccessView.text = formatAverageFacePerRoll(statistics.averageSuccess)
                averageBoonView.text = formatAverageFacePerRoll(statistics.averageBoon)
                averageSigmarView.text = formatAverageFacePerRoll(statistics.averageBoon)
                averageFailureView.text = formatAverageFacePerRoll(statistics.averageFailure)
                averageBaneView.text = formatAverageFacePerRoll(statistics.averageBane)
                averageDelayView.text = formatAverageFacePerRoll(statistics.averageDelay)
                averageExhaustionView.text = formatAverageFacePerRoll(statistics.averageExhaustion)
                averageChaosView.text = formatAverageFacePerRoll(statistics.averageChaos)

                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun formatAverageFacePerRoll(value: Double) =
            getString(R.string.average_face_count_per_roll).format(value.formatWithDigits(1))

    private fun Double.formatWithDigits(digits: Int = 2) = String.format("%.${digits}f", this)
}