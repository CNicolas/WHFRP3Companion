package com.nicolas.whfrp3companion.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.dicelauncher.launch.launch
import com.nicolas.whfrp3companion.DIALOG_LAUNCH_RESULT_TAG
import com.nicolas.whfrp3companion.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.HAND_LAUNCH_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.dialogs.LaunchResultDialog
import com.nicolas.whfrp3database.HandFacade
import com.nicolas.whfrp3database.entities.hand.Hand
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor

class DiceLauncherActivity : AppCompatActivity() {
    @BindView(R.id.hand_name)
    lateinit var handNameView: EditText

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

    private lateinit var handFacade: HandFacade
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

        handFacade = HandFacade(this)
        setViewValues(hand)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_dice_launcher, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset_hand -> reset()
            R.id.save_hand -> saveHand()
            R.id.delete_hand -> deleteHand()
            R.id.launch_statistics_100 -> launchHandStatistics(100)
            R.id.launch_statistics_1000 -> launchHandStatistics(1000)
            R.id.launch_statistics_5000 -> launchHandStatistics(5000)
        }
        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.fab_list_hands)
    fun listHands() {
        alert {
            title = getString(R.string.select_hand)
            items(handFacade.findAll()
                    .map { it.name }) { _, item, _ ->
                setViewValues(handFacade.find(item)!!)
            }
        }.show()
    }

    @OnClick(R.id.launch_button)
    fun launchHand() {
        val launchResultsDialog = LaunchResultDialog(getHandFromPickers().launch())
        launchResultsDialog.show(supportFragmentManager, DIALOG_LAUNCH_RESULT_TAG)
    }

    private fun launchHandStatistics(launchCount: Int) {
        startActivity(intentFor<DiceLauncherStatisticsActivity>(
                HAND_INTENT_ARGUMENT to getHandFromPickers(),
                HAND_LAUNCH_COUNT_INTENT_ARGUMENT to launchCount
        ))
    }

    private fun reset() {
        setViewValues(Hand(""))
    }

    private fun saveHand() {
        handFacade.save(getHandFromPickers())
    }

    private fun deleteHand() {
        handFacade.delete(handNameView.text.toString())
    }

    private fun getHandFromPickers(): Hand =
            Hand(handNameView.text.toString(),
                    characteristicDicesCount = characteristicDicePicker.value,
                    expertiseDicesCount = expertiseDicePicker.value,
                    fortuneDicesCount = fortuneDicePicker.value,
                    conservativeDicesCount = conservativeDicePicker.value,
                    recklessDicesCount = recklessDicePicker.value,
                    challengeDicesCount = challengeDicePicker.value,
                    misfortuneDicesCount = misfortuneDicePicker.value)

    private fun setViewValues(hand: Hand) {
        handNameView.setText(hand.name)
        characteristicDicePicker.value = hand.characteristicDicesCount
        expertiseDicePicker.value = hand.expertiseDicesCount
        fortuneDicePicker.value = hand.fortuneDicesCount
        conservativeDicePicker.value = hand.conservativeDicesCount
        recklessDicePicker.value = hand.recklessDicesCount
        challengeDicePicker.value = hand.challengeDicesCount
        misfortuneDicePicker.value = hand.misfortuneDicesCount
    }
}
