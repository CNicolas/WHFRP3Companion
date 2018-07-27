package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.nicolas.database.HandRepository
import com.nicolas.diceroller.roll.roll
import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_ROLL_RESULT_TAG
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.HAND_ROLL_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import kotlinx.android.synthetic.main.activity_dice_roller.*
import kotlinx.android.synthetic.main.content_dice_roller.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class DiceRollerActivity : AppCompatActivity() {
    private val handRepository by inject<HandRepository>()

    private val emptyHand = Hand("")

    private lateinit var hand: Hand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roller)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        hand = if (intent?.extras !== null) {
            intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
        } else {
            emptyHand
        }

        setupViewsEvents()
        fillViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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

    private fun setupViewsEvents() {
        fab_save_hand.setOnClickListener { saveHand() }

        load_hand.setOnClickListener { listHands() }

        roll_button.setOnClickListener {
            val rollResultsDialog = RollResultDialog.newInstance(hand.roll())
            rollResultsDialog.show(supportFragmentManager, DIALOG_ROLL_RESULT_TAG)
        }

        handNameTextView.addTextChangedListener(handNameTextWatcher)
        characteristicDicePicker.setOnValueChangedListener { _, _, newVal -> hand.characteristicDicesCount = newVal }
        expertiseDicePicker.setOnValueChangedListener { _, _, newVal -> hand.expertiseDicesCount = newVal }
        fortuneDicePicker.setOnValueChangedListener { _, _, newVal -> hand.fortuneDicesCount = newVal }
        conservativeDicePicker.setOnValueChangedListener { _, _, newVal -> hand.conservativeDicesCount = newVal }
        recklessDicePicker.setOnValueChangedListener { _, _, newVal -> hand.recklessDicesCount = newVal }
        challengeDicePicker.setOnValueChangedListener { _, _, newVal -> hand.challengeDicesCount = newVal }
        misfortuneDicePicker.setOnValueChangedListener { _, _, newVal -> hand.misfortuneDicesCount = newVal }
    }

    private fun listHands() {
        val allHands = handRepository.findAll()

        if (allHands.isEmpty()) {
            toast(R.string.no_saved_hand)
        } else {
            alert {
                title = getString(R.string.select_hand)
                items(allHands.map { it.name }) { _, item, _ ->
                    hand = handRepository.find(item)!!

                    fillViews()
                }
            }.show()
        }
    }

    private fun rollHandStatistics(rollCount: Int) {
        startActivity(intentFor<DiceRollerStatisticsActivity>(
                HAND_INTENT_ARGUMENT to hand,
                HAND_ROLL_COUNT_INTENT_ARGUMENT to rollCount
        ))
    }

    private fun reset() {
        hand = emptyHand
        fillViews()
    }

    private fun saveHand() {
        handRepository.save(hand)
    }

    private fun deleteHand() {
        handRepository.delete(hand.name)
        reset()
    }

    private fun fillViews() {
        handNameTextView.setText(hand.name)
        characteristicDicePicker.value = hand.characteristicDicesCount
        expertiseDicePicker.value = hand.expertiseDicesCount
        fortuneDicePicker.value = hand.fortuneDicesCount
        conservativeDicePicker.value = hand.conservativeDicesCount
        recklessDicePicker.value = hand.recklessDicesCount
        challengeDicePicker.value = hand.challengeDicesCount
        misfortuneDicePicker.value = hand.misfortuneDicesCount
    }

    private val handNameTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                hand.name = s.toString()
            }
        }
}
