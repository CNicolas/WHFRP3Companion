package com.nicolas.whfrp3companion.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.diceroller.roll.rollForStatistics
import com.nicolas.whfrp3companion.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.HAND_ROLL_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.hand.Hand
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DiceRollerStatisticsActivity : AppCompatActivity() {
    @BindView(R.id.total_roll_count)
    lateinit var totalRollCount: TextView
    @BindView(R.id.successful_rolls)
    lateinit var successfulRolls: TextView

    @BindView(R.id.average_success)
    lateinit var averageSuccess: TextView
    @BindView(R.id.average_boon)
    lateinit var averageBoon: TextView
    @BindView(R.id.average_sigmar)
    lateinit var averageSigmar: TextView
    @BindView(R.id.average_failure)
    lateinit var averageFailure: TextView
    @BindView(R.id.average_bane)
    lateinit var averageBane: TextView
    @BindView(R.id.average_delay)
    lateinit var averageDelay: TextView
    @BindView(R.id.average_exhaustion)
    lateinit var averageExhaustion: TextView
    @BindView(R.id.average_chaos)
    lateinit var averageChaos: TextView

    @BindView(R.id.swipe_refresh_layout)
    lateinit var refreshLayout: SwipeRefreshLayout

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
                        .format(statistics.successfulRollCount, statistics.successfulPercentage)

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
            getString(R.string.average_face_count_per_roll).format(value)
}