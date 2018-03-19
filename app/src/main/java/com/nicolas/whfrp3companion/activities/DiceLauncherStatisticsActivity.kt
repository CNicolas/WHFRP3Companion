package com.nicolas.whfrp3companion.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.dicelauncher.launch.launchForStatistics
import com.nicolas.whfrp3companion.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.HAND_LAUNCH_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.hand.Hand
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DiceLauncherStatisticsActivity : AppCompatActivity() {
    @BindView(R.id.total_launch_count)
    lateinit var totalLaunchCount: TextView
    @BindView(R.id.successful_launches)
    lateinit var successfulLaunches: TextView

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
    private var launchCount: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_launcher_statistics)

        unbinder = ButterKnife.bind(this)
        refreshLayout.setOnRefreshListener { calculateStatistics() }

        if (intent?.extras !== null) {
            hand = intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
            launchCount = intent.extras.getInt(HAND_LAUNCH_COUNT_INTENT_ARGUMENT)
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
            val statistics = hand.launchForStatistics(launchCount)
            uiThread {
                totalLaunchCount.text = "${statistics.launchCount} launches"
                successfulLaunches.text = "${statistics.successfulLaunchCount} successful launches (${statistics.successfulPercentage}%)"

                averageSuccess.text = "${statistics.averageSuccess} per launch"
                averageBoon.text = "${statistics.averageBoon} per launch"
                averageSigmar.text = "${statistics.averageBoon} per launch"
                averageFailure.text = "${statistics.averageFailure} per launch"
                averageBane.text = "${statistics.averageBane} per launch"
                averageDelay.text = "${statistics.averageDelay} per launch"
                averageExhaustion.text = "${statistics.averageExhaustion} per launch"
                averageChaos.text = "${statistics.averageChaos} per launch"

                refreshLayout.isRefreshing = false
            }
        }
    }
}