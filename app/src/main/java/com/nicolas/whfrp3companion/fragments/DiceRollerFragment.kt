package com.nicolas.whfrp3companion.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.nicolas.database.HandRepository
import com.nicolas.diceroller.roll.roll
import com.nicolas.models.dice.DiceType
import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.diceRoller.SimpleProgressChangeListener
import com.nicolas.whfrp3companion.shared.DIALOG_ROLL_RESULT_TAG
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.HAND_ROLL_COUNT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.DiceRollerStatisticsActivity
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import kotlinx.android.synthetic.main.content_dice_roller_bars.*
import kotlinx.android.synthetic.main.fragment_dice_roller.*
import org.jetbrains.anko.*
import org.koin.android.ext.android.inject

class DiceRollerFragment : Fragment() {
    private val handRepository by inject<HandRepository>()

    private var hand: Hand = Hand("")

    private lateinit var hands: List<Hand>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_dice_roller, container, false)
        setHasOptionsMenu(true)

        doAsync {
            hands = handRepository.findAll()
        }

        setupViewsEvents()
        setHandAndFillViews()

        return resultingView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_dice_roller, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_hand -> saveHand()
            R.id.delete_hand -> deleteHand()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewsEvents() {
        activity?.let { safeActivity ->
            load_hand.setOnClickListener { listHands() }

            fab_roll_hand.setOnClickListener { rollHand() }
            fab_roll_hand.setOnLongClickListener {
                val selectorItems = listOf(getString(R.string.roll_100_times),
                        getString(R.string.roll_1000_times),
                        getString(R.string.roll_5000_times))

                safeActivity.selector(getString(R.string.page_dice_roller_statistics), selectorItems) { _, index ->
                    when (index) {
                        0 -> rollHandStatistics(100)
                        1 -> rollHandStatistics(1000)
                        2 -> rollHandStatistics(5000)
                    }
                }

                true
            }

            fab_reset_hand.setOnClickListener { reset() }

            characteristic_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.characteristicDicesCount = newValue
                characteristic_value_button.text = "$newValue"
            })
            conservative_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.conservativeDicesCount = newValue
                conservative_value_button.text = "$newValue"
            })
            reckless_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.recklessDicesCount = newValue
                reckless_value_button.text = "$newValue"
            })
            expertise_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.expertiseDicesCount = newValue
                expertise_value_button.text = "$newValue"
            })
            fortune_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.fortuneDicesCount = newValue
                fortune_value_button.text = "$newValue"
            })
            challenge_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.challengeDicesCount = newValue
                challenge_value_button.text = "$newValue"
            })
            misfortune_bar.setOnProgressChangeListener(SimpleProgressChangeListener { newValue ->
                hand.misfortuneDicesCount = newValue
                misfortune_value_button.text = "$newValue"
            })

            characteristic_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.CHARACTERISTIC) }
            conservative_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.CONSERVATIVE) }
            reckless_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.RECKLESS) }
            expertise_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.EXPERTISE) }
            fortune_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.FORTUNE) }
            challenge_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.CHALLENGE) }
            misfortune_value_button.setOnClickListener { openChangeDiceValueDialog(DiceType.MISFORTUNE) }
        }
    }

    private fun reset() {
        characteristic_bar.max = 8
        conservative_bar.max = 8
        reckless_bar.max = 8
        expertise_bar.max = 8
        fortune_bar.max = 8
        challenge_bar.max = 8
        misfortune_bar.max = 8

        setHandAndFillViews(Hand(""))
    }

    private fun setHandAndFillViews(newHand: Hand = this.hand) {
        this.hand = newHand

        activity?.let {
            if (hand.characteristicDicesCount > characteristic_bar.max) {
                characteristic_bar.max = hand.characteristicDicesCount
            }

            if (hand.conservativeDicesCount > conservative_bar.max) {
                conservative_bar.max = hand.conservativeDicesCount
            }

            if (hand.recklessDicesCount > reckless_bar.max) {
                reckless_bar.max = hand.recklessDicesCount
            }

            if (hand.expertiseDicesCount > expertise_bar.max) {
                expertise_bar.max = hand.expertiseDicesCount
            }

            if (hand.fortuneDicesCount > fortune_bar.max) {
                fortune_bar.max = hand.fortuneDicesCount
            }

            if (hand.challengeDicesCount > challenge_bar.max) {
                challenge_bar.max = hand.challengeDicesCount
            }

            if (hand.misfortuneDicesCount > misfortune_bar.max) {
                misfortune_bar.max = hand.misfortuneDicesCount
            }

            characteristic_bar.progress = hand.characteristicDicesCount
            conservative_bar.progress = hand.conservativeDicesCount
            reckless_bar.progress = hand.recklessDicesCount
            expertise_bar.progress = hand.expertiseDicesCount
            fortune_bar.progress = hand.fortuneDicesCount
            challenge_bar.progress = hand.challengeDicesCount
            misfortune_bar.progress = hand.misfortuneDicesCount

            hand_name.setText(hand.name)
        }
    }

    private fun listHands() {
        activity?.also { safeActivity ->
            if (hands.isEmpty()) {
                safeActivity.toast(R.string.no_saved_hand)
            } else {
                safeActivity.alert {
                    title = getString(R.string.select_hand)

                    val handsNames = hands.map { it.name }
                    items(handsNames) { _, _, index ->
                        setHandAndFillViews(hands[index])
                    }
                }.show()
            }
        }
    }

    private fun openChangeDiceValueDialog(diceType: DiceType) {
        val counts = mutableListOf<String>()
        (20 downTo 8).forEach { counts.add("$it") }

        activity?.selector(getString(R.string.dices_count), counts) { _, index ->
            val newHand = hand.copy()
            newHand[diceType] = counts[index].toInt()

            setHandAndFillViews(newHand)
        }
    }

    private fun saveHand() {
        doAsync {
            hand = handRepository.save(hand)
        }
    }

    private fun deleteHand() {
        doAsync {
            handRepository.delete(hand.name)

            uiThread {
                reset()
            }
        }
    }

    private fun rollHand() {
        val rollResultsDialog = RollResultDialog.newInstance(hand.roll())

        activity?.let {
            rollResultsDialog.show(it.supportFragmentManager, DIALOG_ROLL_RESULT_TAG)
        }
    }

    private fun rollHandStatistics(rollCount: Int) {
        activity?.let {
            startActivity(it.intentFor<DiceRollerStatisticsActivity>(
                    HAND_INTENT_ARGUMENT to hand,
                    HAND_ROLL_COUNT_INTENT_ARGUMENT to rollCount
            ))
        }
    }

    companion object {
        fun newInstance(): DiceRollerFragment {
            val args = Bundle()
            val fragment = DiceRollerFragment()
            fragment.arguments = args

            return fragment
        }
    }
}