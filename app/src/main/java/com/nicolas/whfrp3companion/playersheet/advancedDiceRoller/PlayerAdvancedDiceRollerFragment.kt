package com.nicolas.whfrp3companion.playersheet.advancedDiceRoller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import com.nicolas.database.PlayerRepository
import com.nicolas.diceroller.roll.roll
import com.nicolas.models.action.Action
import com.nicolas.models.extensions.applyStanceDices
import com.nicolas.models.extensions.createHand
import com.nicolas.models.hand.Hand
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.Player
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.actions.ActionRollResultDialog
import com.nicolas.whfrp3companion.playersheet.state.StanceChangeListener
import com.nicolas.whfrp3companion.shared.*
import com.nicolas.whfrp3companion.shared.activities.DiceRollerStatisticsActivity
import com.nicolas.whfrp3companion.shared.components.NumberPickerMinMax
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import kotlinx.android.synthetic.main.content_stance_bar.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject
import kotlin.math.abs


class PlayerAdvancedDiceRollerFragment : Fragment() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player
    private lateinit var hand: Hand

    private var skill: Skill? = null
    private var specialization: Specialization? = null
    private var action: Action? = null
    private var weapon: Weapon? = null

    private val emptyHand = Hand("")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_advanced_dice_roller, container, false)
        setHasOptionsMenu(true)

        hand = emptyHand

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerName?.let {
            doAsync {
                player = playerRepository.find(it)!!

                uiThread {
                    setupStance()
                    setupViewEvents(resultingView)
                }
            }
        }

        return resultingView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_player_advanced_dice_roller, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset_hand -> reset()
            R.id.roll_statistics_100 -> rollHandStatistics(100)
            R.id.roll_statistics_1000 -> rollHandStatistics(1000)
            R.id.roll_statistics_5000 -> rollHandStatistics(5000)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTION_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                action = data?.getSerializableExtra(ACTION_INTENT_ARGUMENT) as Action?
                action?.let {
                    val actionHand = player.createHand(it)
                    actionHand?.let {
                        hand = it
                    }
                }
                weapon = data?.getSerializableExtra(WEAPON_INTENT_ARGUMENT) as Weapon?
            }
            SKILL_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                skill = data?.getSerializableExtra(SKILL_INTENT_ARGUMENT) as Skill?
                specialization = data?.getSerializableExtra(SPECIALIZATION_INTENT_ARGUMENT) as Specialization?

                skill?.let { skill ->
                    specialization?.let {
                        hand = player.createHand(skill, it)
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupViewEvents(view: View) {
        view.getView<FloatingActionButton>(R.id.fab_roll_hand)
                .setOnClickListener { rollHand() }

        view.getView<NumberPickerMinMax>(R.id.characteristicDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.characteristicDicesCount = newVal }
        view.getView<NumberPickerMinMax>(R.id.expertiseDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.expertiseDicesCount = newVal }
        view.getView<NumberPickerMinMax>(R.id.fortuneDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.fortuneDicesCount = newVal }
        view.getView<NumberPickerMinMax>(R.id.conservativeDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.conservativeDicesCount = newVal }
        view.getView<NumberPickerMinMax>(R.id.recklessDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.recklessDicesCount = newVal }
        view.getView<NumberPickerMinMax>(R.id.challengeDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.challengeDicesCount = newVal }
        view.getView<NumberPickerMinMax>(R.id.misfortuneDicePicker)
                .setOnValueChangedListener { _, _, newVal -> hand.misfortuneDicesCount = newVal }

        view.getView<Button>(R.id.select_skill_button)
                .setOnClickListener { openSkillsSelection() }
        view.getView<Button>(R.id.select_action_button)
                .setOnClickListener { openActionsSelection() }
    }

    private fun fillViews() {
        activity?.let {
            it.getView<NumberPickerMinMax>(R.id.characteristicDicePicker).value = hand.characteristicDicesCount
            it.getView<NumberPickerMinMax>(R.id.expertiseDicePicker).value = hand.expertiseDicesCount
            it.getView<NumberPickerMinMax>(R.id.fortuneDicePicker).value = hand.fortuneDicesCount
            it.getView<NumberPickerMinMax>(R.id.conservativeDicePicker).value = hand.conservativeDicesCount
            it.getView<NumberPickerMinMax>(R.id.recklessDicePicker).value = hand.recklessDicesCount
            it.getView<NumberPickerMinMax>(R.id.challengeDicePicker).value = hand.challengeDicesCount
            it.getView<NumberPickerMinMax>(R.id.misfortuneDicePicker).value = hand.misfortuneDicesCount
        }
    }

    private fun reset() {
        hand = emptyHand
        fillViews()
        changeStance(0)
    }

    private fun rollHand() {
        val rollResultsDialog = action?.let {
            ActionRollResultDialog.newInstance(hand.roll(), it, player.name, weapon)
        } ?: {
            RollResultDialog.newInstance(hand.roll())
        }()

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

    private fun openSkillsSelection() {
        activity?.let {
            startActivityForResult(it.intentFor<PlayerAdvancedDiceRollerSkillsActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name
            ), SKILL_REQUEST_CODE)
        }
    }

    private fun openActionsSelection() {
        activity?.let {
            startActivityForResult(it.intentFor<PlayerAdvancedDiceRollerActionsActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name
            ), ACTION_REQUEST_CODE)
        }
    }

    private fun setupStance() {
        activity?.let {
            stanceBar.min = -player.maxConservative
            stanceBar.max = player.maxReckless

            val stanceChangeListener = StanceChangeListener(it,
                    { currentStanceTextView.setTextColor(it) },
                    { changeStance(it) })
            stanceBar.setOnProgressChangeListener(stanceChangeListener)

            stanceBar.progress = player.stance
            stanceBar.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
                override fun transform(value: Int): Int = abs(value)
            }
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

    companion object {
        fun newInstance(playerName: String): PlayerAdvancedDiceRollerFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerAdvancedDiceRollerFragment()
            fragment.arguments = args

            return fragment
        }
    }
}