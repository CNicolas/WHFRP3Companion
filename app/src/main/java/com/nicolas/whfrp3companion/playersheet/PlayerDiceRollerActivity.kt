package com.nicolas.whfrp3companion.playersheet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nicolas.database.HandRepository
import com.nicolas.database.PlayerRepository
import com.nicolas.diceroller.roll.roll
import com.nicolas.models.extensions.applyStanceDices
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.state.StanceChangeListener
import com.nicolas.whfrp3companion.shared.DIALOG_ROLL_RESULT_TAG
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.HAND_ROLL_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.DiceRollerStatisticsActivity
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import kotlinx.android.synthetic.main.activity_dice_roller.*
import kotlinx.android.synthetic.main.content_dice_roller.*
import kotlinx.android.synthetic.main.content_stance_bar.*
import kotlinx.android.synthetic.main.toolbar.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.*
import org.koin.android.ext.android.inject
import kotlin.math.abs

internal class PlayerDiceRollerActivity : AppCompatActivity() {
    private val playerRepository by inject<PlayerRepository>()
    private val handRepository by inject<HandRepository>()

    private val emptyHand = Hand("")

    private lateinit var hand: Hand
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_dice_roller)

        var playerName: String? = null
        if (intent?.extras !== null) {
            hand = intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
            playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        } else {
            hand = emptyHand
        }

        playerName?.let {
            doAsync {
                player = playerRepository.find(it)!!

                uiThread {
                    setupStance()
                    setupViewsEvents()
                    fillViews()
                }
            }
        }

        setSupportActionBar(toolbar)
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
            val rollResultsDialog = RollResultDialog.newInstance(getHandFromPickers().roll())
            rollResultsDialog.show(supportFragmentManager, DIALOG_ROLL_RESULT_TAG)
        }

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
                HAND_INTENT_ARGUMENT to getHandFromPickers(),
                HAND_ROLL_COUNT_INTENT_ARGUMENT to rollCount
        ))
    }

    private fun reset() {
        hand = emptyHand

        fillViews()
    }

    private fun saveHand() {
        handRepository.save(getHandFromPickers())
    }

    private fun deleteHand() {
        handRepository.delete(handNameView.text.toString())
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

    private fun fillViews() {
        handNameView.setText(hand.name)
        characteristicDicePicker.value = hand.characteristicDicesCount
        expertiseDicePicker.value = hand.expertiseDicesCount
        fortuneDicePicker.value = hand.fortuneDicesCount
        conservativeDicePicker.value = hand.conservativeDicesCount
        recklessDicePicker.value = hand.recklessDicesCount
        challengeDicePicker.value = hand.challengeDicesCount
        misfortuneDicePicker.value = hand.misfortuneDicesCount
    }

    private fun setupStance() {
        stanceBar.min = -player.maxConservative
        stanceBar.max = player.maxReckless

        val stanceChangeListener = StanceChangeListener(this,
                { currentStanceTextView.setTextColor(it) },
                { changeStance(it) })
        stanceBar.setOnProgressChangeListener(stanceChangeListener)

        stanceBar.progress = player.stance
        stanceBar.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
            override fun transform(value: Int): Int = abs(value)
        }
    }

    private fun changeStance(newStanceValue: Int) {
        currentStanceTextView.text = Math.abs(newStanceValue).toString()
        player.stance = newStanceValue

        doAsync {
            player = playerRepository.update(player)
            hand = hand.applyStanceDices(player.stance)

            uiThread {
                fillViews()
            }
        }
    }
}
