package com.nicolas.whfrp3companion.playersheet.advancedDiceRoller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.nicolas.database.PlayerRepository
import com.nicolas.diceroller.roll.roll
import com.nicolas.models.action.Action
import com.nicolas.models.dice.DiceType
import com.nicolas.models.extensions.applyStanceDices
import com.nicolas.models.extensions.createHand
import com.nicolas.models.hand.Hand
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.actions.ActionRollResultDialog
import com.nicolas.whfrp3companion.playersheet.state.StanceChangeListener
import com.nicolas.whfrp3companion.shared.*
import com.nicolas.whfrp3companion.shared.activities.DiceRollerStatisticsActivity
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import com.nicolas.whfrp3companion.shared.enums.labelId
import kotlinx.android.synthetic.main.content_stance_bar.*
import kotlinx.android.synthetic.main.fragment_player_advanced_dice_roller.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.selector
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject
import kotlin.math.abs


class PlayerAdvancedDiceRollerFragment : Fragment() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player
    private var hand: Hand = Hand("")

    private var skill: Skill? = null
    private var specialization: Specialization? = null
    private var action: Action? = null
    private var weapon: Weapon? = null

    // region Overrides

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_advanced_dice_roller, container, false)
        setHasOptionsMenu(true)

        arguments?.let { args ->
            val playerName = args.getString(PLAYER_NAME_INTENT_ARGUMENT)
            playerName?.let {
                doAsync {
                    player = playerRepository.find(it)!!

                    uiThread { _ ->
                        setupViewsEvents()
                        setupStanceBar()

                        handleBundle(args)

                        // The if under is trying to refresh the progress to the current stance and color
                        val realStance = player.stance
                        if (player.stance != stanceBar.max) {
                            stanceBar.progress = stanceBar.max
                        } else {
                            stanceBar.progress = stanceBar.min
                        }
                        stanceBar.progress = realStance
                    }
                }
            }
        }

        return resultingView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_player_advanced_dice_roller, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.select_action -> openActionsSelection()
            R.id.select_characteristic -> openCharacteristicSelection()
            R.id.select_skill -> openSkillsSelection()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTION_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                action = data?.getSerializableExtra(ACTION_INTENT_ARGUMENT) as Action?
                weapon = data?.getSerializableExtra(WEAPON_INTENT_ARGUMENT) as Weapon?
                receiveSelectedAction()
            }
            SKILL_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                skill = data?.getSerializableExtra(SKILL_INTENT_ARGUMENT) as Skill?
                specialization = data?.getSerializableExtra(SPECIALIZATION_INTENT_ARGUMENT) as Specialization?
                receiveSelectedSkill()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    // endregion

    // region Initialization

    private fun handleBundle(args: Bundle) {
        when {
            args.containsKey(ACTION_INTENT_ARGUMENT) -> {
                action = args.getSerializable(ACTION_INTENT_ARGUMENT) as Action?
                receiveSelectedAction()
            }
            args.containsKey(SKILL_INTENT_ARGUMENT) -> {
                skill = args.getSerializable(SKILL_INTENT_ARGUMENT) as Skill?

                if (args.containsKey(SPECIALIZATION_INTENT_ARGUMENT)) {
                    specialization = args.getSerializable(SPECIALIZATION_INTENT_ARGUMENT) as Specialization?
                }

                receiveSelectedSkill()
            }
            args.containsKey(CHARACTERISTIC_INTENT_ARGUMENT) -> {
                val characteristic = args.getSerializable(CHARACTERISTIC_INTENT_ARGUMENT) as Characteristic
                receiveSelectedCharacteristic(characteristic)
            }
            else -> setHandAndFillViews()
        }
    }

    private fun setupViewsEvents() {
        activity?.let { safeActivity ->
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

            val stanceChangeListener = StanceChangeListener(safeActivity,
                    { current_stance.setTextColor(it) },
                    { changeStance(it) })
            stanceBar.setOnProgressChangeListener(stanceChangeListener)
        }
    }

    private fun setupStanceBar() {
        activity?.let {
            stanceBar.min = -player.maxConservative
            stanceBar.max = player.maxReckless

            stanceBar.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
                override fun transform(value: Int): Int = abs(value)
            }
        }
    }

    // endregion

    // region UI events

    private fun reset() {
        stanceBar.progress = 0

        characteristic_bar.max = 8
        conservative_bar.max = 8
        reckless_bar.max = 8
        expertise_bar.max = 8
        fortune_bar.max = 8
        challenge_bar.max = 8
        misfortune_bar.max = 8

        setHandAndFillViews(Hand(""))
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


    private fun openActionsSelection() {
        activity?.let {
            startActivityForResult(it.intentFor<PlayerAdvancedDiceRollerActionsActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name
            ), ACTION_REQUEST_CODE)
        }
    }

    private fun openCharacteristicSelection() {
        val title = getString(R.string.characteristic)
        val characteristicsAsStrings = Characteristic.values().map { getString(it.labelId) }

        activity?.selector(title, characteristicsAsStrings) { _, index ->
            receiveSelectedCharacteristic(Characteristic[index])
        }
    }

    private fun openSkillsSelection() {
        activity?.let {
            startActivityForResult(it.intentFor<PlayerAdvancedDiceRollerSkillsActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name
            ), SKILL_REQUEST_CODE)
        }
    }

    // endregion

    // region Handle hand change

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

            current_hand_name.text = hand.name
        }
    }

    private fun receiveSelectedAction() {
        action?.let { actionNotNull ->
            val actionHand = player.createHand(actionNotNull)

            actionHand?.let { hand ->
                setHandAndFillViews(hand)

                weapon?.let { weaponNotNull ->
                    current_hand_name.text = getString(R.string.text_double_dots_text_format)
                            .format(actionNotNull.name, weaponNotNull.name)
                } ?: {
                    current_hand_name.text = actionNotNull.name
                }()

                skill = null
                specialization = null
            }
        }
    }

    private fun receiveSelectedSkill() {
        skill?.let { skillNotNull ->
            specialization?.let { specializationNotNull ->
                setHandAndFillViews(player.createHand(skillNotNull, specializationNotNull))

                current_hand_name.text = getString(R.string.text_double_dots_text_format)
                        .format(skillNotNull.name, specializationNotNull.name)

                action = null
                weapon = null

            } ?: {
                setHandAndFillViews(player.createHand(skillNotNull))

                current_hand_name.text = skillNotNull.name

                action = null
                weapon = null
            }()
        }
    }

    private fun receiveSelectedCharacteristic(characteristic: Characteristic) {
        val characteristicHand = player.createHand(characteristic)
        setHandAndFillViews(characteristicHand)

        current_hand_name.text = getString(characteristic.labelId)

        action = null
        weapon = null
        skill = null
        specialization = null
    }

    private fun changeStance(newStanceValue: Int) {
        current_stance.text = Math.abs(newStanceValue).toString()
        player.stance = newStanceValue

        doAsync {
            player = playerRepository.update(player)

            uiThread {
                setHandAndFillViews(hand.applyStanceDices(player.stance))
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

    // endregion

    // region Instance static creators

    companion object {
        fun newInstance(playerName: String): PlayerAdvancedDiceRollerFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerAdvancedDiceRollerFragment()
            fragment.arguments = args

            return fragment
        }

        fun newInstance(playerName: String, action: Action): PlayerAdvancedDiceRollerFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)
            args.putSerializable(ACTION_INTENT_ARGUMENT, action)

            val fragment = PlayerAdvancedDiceRollerFragment()
            fragment.arguments = args

            return fragment
        }

        fun newInstance(playerName: String, skill: Skill, specialization: Specialization? = null): PlayerAdvancedDiceRollerFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)
            args.putSerializable(SKILL_INTENT_ARGUMENT, skill)
            args.putSerializable(SPECIALIZATION_INTENT_ARGUMENT, specialization)

            val fragment = PlayerAdvancedDiceRollerFragment()
            fragment.arguments = args

            return fragment
        }

        fun newInstance(playerName: String, characteristic: Characteristic): PlayerAdvancedDiceRollerFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)
            args.putSerializable(CHARACTERISTIC_INTENT_ARGUMENT, characteristic)

            val fragment = PlayerAdvancedDiceRollerFragment()
            fragment.arguments = args

            return fragment
        }
    }

    // endregion
}