package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.diceroller.roll.roll
import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_ROLL_RESULT_TAG
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.HAND_ROLL_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import com.nicolas.whfrp3database.anko.HandFacade
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class DiceRollerActivity : AppCompatActivity() {
    private val handNameView by bind<EditText>(R.id.hand_name)

    private val characteristicDicePicker by bind<NumberPicker>(R.id.characteristic_dice_picker)
    private val conservativeDicePicker by bind<NumberPicker>(R.id.conservative_dice_picker)
    private val recklessDicePicker by bind<NumberPicker>(R.id.reckless_dice_picker)
    private val expertiseDicePicker by bind<NumberPicker>(R.id.expertise_dice_picker)
    private val fortuneDicePicker by bind<NumberPicker>(R.id.fortune_dice_picker)
    private val challengeDicePicker by bind<NumberPicker>(R.id.challenge_dice_picker)
    private val misfortuneDicePicker by bind<NumberPicker>(R.id.misfortune_dice_picker)

    private lateinit var unbinder: Unbinder

    private lateinit var handFacade: HandFacade
    private lateinit var hand: Hand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roller)

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
        menuInflater.inflate(R.menu.activity_dice_roller, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset_hand -> reset()
            R.id.delete_hand -> deleteHand()
            R.id.roll_statistics_100 -> rollHandStatistics(100)
            R.id.roll_statistics_1000 -> rollHandStatistics(1000)
            R.id.roll_statistics_5000 -> rollHandStatistics(5000)
        }
        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.fab_save_hand)
    fun clickSaveHand() {
        saveHand()
    }

    @OnClick(R.id.load_hand)
    fun listHands() {
        val allHands = handFacade.findAll()

        if (allHands.isEmpty()) {
            toast(R.string.no_saved_hand)
        } else {
            alert {
                title = getString(R.string.select_hand)
                items(allHands.map { it.name },
                        { _, item, _ ->
                            setViewValues(handFacade.find(item)!!)
                        }
                )
            }.show()
        }
    }

    @OnClick(R.id.roll_button)
    fun rollHand() {
        val rollResultsDialog = RollResultDialog(getHandFromPickers().roll())
        rollResultsDialog.show(supportFragmentManager, DIALOG_ROLL_RESULT_TAG)
    }

    private fun rollHandStatistics(rollCount: Int) {
        startActivity(intentFor<DiceRollerStatisticsActivity>(
                HAND_INTENT_ARGUMENT to getHandFromPickers(),
                HAND_ROLL_COUNT_INTENT_ARGUMENT to rollCount
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
        reset()
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
