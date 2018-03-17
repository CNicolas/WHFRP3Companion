package com.nicolas.whfrp3companion.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.NumberPicker
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.dicelauncher.launch.launch
import com.nicolas.whfrp3companion.DIALOG_LAUNCH_RESULT_TAG
import com.nicolas.whfrp3companion.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.dialogs.LaunchResultDialog
import com.nicolas.whfrp3database.entities.hand.Hand
import org.jetbrains.anko.longToast

class DiceLauncherActivity : AppCompatActivity() {
    @BindView(R.id.characteristic_dice_picker)
    lateinit var characteristicDicePicker: NumberPicker
    @BindView(R.id.conservative_dice_picker)
    lateinit var conservativeDicePicker: NumberPicker
    @BindView(R.id.reckless_dice_picker)
    lateinit var recklessDicePicker: NumberPicker
    @BindView(R.id.expertise_dice_picker)
    lateinit var expertiseDicePicker: NumberPicker
    @BindView(R.id.fortune_dice_picker)
    lateinit var fortuneDicePicker: NumberPicker
    @BindView(R.id.challenge_dice_picker)
    lateinit var challengeDicePicker: NumberPicker
    @BindView(R.id.misfortune_dice_picker)
    lateinit var misfortuneDicePicker: NumberPicker

    private lateinit var unbinder: Unbinder

    private lateinit var hand: Hand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_launcher)

        unbinder = ButterKnife.bind(this)

        hand = if (intent?.extras !== null) {
            intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
        } else {
            Hand("")
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onContextItemSelected(item)
    }

    @OnClick(R.id.fab_launch_hand)
    fun launchHand() {
        val hand = Hand("No name",
                characteristicDicesCount = characteristicDicePicker.value,
                expertiseDicesCount = expertiseDicePicker.value,
                fortuneDicesCount = fortuneDicePicker.value,
                conservativeDicesCount = conservativeDicePicker.value,
                recklessDicesCount = recklessDicePicker.value,
                challengeDicesCount = challengeDicePicker.value,
                misfortuneDicesCount = misfortuneDicePicker.value)

        val launchResultsDialog = LaunchResultDialog(hand.launch())
        launchResultsDialog.show(supportFragmentManager, DIALOG_LAUNCH_RESULT_TAG)
    }

    @OnClick(R.id.fab_launch_hand_statistics)
    fun launchHandStatistics() {
        longToast("How many times ?")
    }
}
