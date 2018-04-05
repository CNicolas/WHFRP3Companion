package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.diceroller.roll.rollForStatistics
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.HAND_ROLL_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3database.entities.hand.Hand
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DiceRollerStatisticsActivity : AppCompatActivity() {
    private val totalRollCount by bind<TextView>(R.id.total_roll_count)
    private val successfulRolls by bind<TextView>(R.id.successful_rolls)

    private val averageSuccess by bind<TextView>(R.id.average_success)
    private val averageBoon by bind<TextView>(R.id.average_boon)
    private val averageSigmar by bind<TextView>(R.id.average_sigmar)
    private val averageFailure by bind<TextView>(R.id.average_failure)
    private val averageBane by bind<TextView>(R.id.average_bane)
    private val averageDelay by bind<TextView>(R.id.average_delay)
    private val averageExhaustion by bind<TextView>(R.id.average_exhaustion)
    private val averageChaos by bind<TextView>(R.id.average_chaos)

    private val refreshLayout by bind<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

    private lateinit var unbinder: Unbinder
    private lateinit var hand: Hand
    private var rollCount: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roller_statistics)

        unbinder = ButterKnife.bind(this)
        refreshLayout.setOnRefreshListener { calculateStatistics() }

        if (intent?.extras !== null) {
            hand = intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
            rollCount = intent.extras.getInt(HAND_ROLL_COUNT_INTENT_ARGUMENT)
        } else {
            hand = Hand("")
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        calculateStatistics()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    private fun calculateStatistics() {
        refreshLayout.isRefreshing = true

        doAsync {
            val statistics = hand.rollForStatistics(rollCount)
            uiThread {
                totalRollCount.text = getString(R.string.rolls_count_format).format(statistics.rollCount)
                successfulRolls.text = getString(R.string.successful_rolls_with_percentage_format)
                        .format(statistics.successfulRollCount, statistics.successfulPercentage.formatWithDigits(2))

                averageSuccess.text = formatAverageFacePerRoll(statistics.averageSuccess)
                averageBoon.text = formatAverageFacePerRoll(statistics.averageBoon)
                averageSigmar.text = formatAverageFacePerRoll(statistics.averageBoon)
                averageFailure.text = formatAverageFacePerRoll(statistics.averageFailure)
                averageBane.text = formatAverageFacePerRoll(statistics.averageBane)
                averageDelay.text = formatAverageFacePerRoll(statistics.averageDelay)
                averageExhaustion.text = formatAverageFacePerRoll(statistics.averageExhaustion)
                averageChaos.text = formatAverageFacePerRoll(statistics.averageChaos)

                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun formatAverageFacePerRoll(value: Double) =
            getString(R.string.average_face_count_per_roll).format(value.formatWithDigits(1))

    private fun Double.formatWithDigits(digits: Int = 2) = String.format("%.${digits}f", this)
}