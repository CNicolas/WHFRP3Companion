package com.nicolas.whfrp3companion.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.hand.Hand

class DiceLauncherStatisticsActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder
    private lateinit var hand: Hand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_launcher_statistics)

        unbinder = ButterKnife.bind(this)

        hand = if (intent?.extras !== null) {
            intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
        } else {
            Hand("")
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }
}