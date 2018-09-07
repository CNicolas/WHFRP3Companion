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
import org.jetbrains.anko.*
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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_advanced_dice_roller, container, false)
        setHasOptionsMenu(true)

        hand = Hand("")

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerName?.let {
            doAsync {
                player = playerRepository.find(it)!!

                uiThread {
                    setupViewsEvents()
                    setupStanceBar()
                    setHandAndFillViews()

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

        return resultingView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_player_advanced_dice_roller, menu)

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
                receiveSelectedAction(data)
            }
            SKILL_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                receiveSelectedSkill(data)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    // region Initialization

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
                    { currentStanceTextView.setTextColor(it) },
                    { changeStance(it) })
            stanceBar.setOnProgressChangeListener(stanceChangeListener)
        }
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
            val characteristicHand = player.createHand(Characteristic[index])
            setHandAndFillViews(characteristicHand)

            val handName: String = (characteristicsAsStrings[index])
            current_hand_name.text = handName
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

    private fun receiveSelectedAction(data: Intent?) {
        action = data?.getSerializableExtra(ACTION_INTENT_ARGUMENT) as Action?
        action?.let {
            val actionHand = player.createHand(it)
            actionHand?.let {
                setHandAndFillViews(it)
            }
        }
        weapon = data?.getSerializableExtra(WEAPON_INTENT_ARGUMENT) as Weapon?
        skill = null
        specialization = null

        val handName: String = (action?.name ?: "") + (weapon?.let { " : ${it.name}" } ?: "")
        current_hand_name.text = handName
    }

    private fun receiveSelectedSkill(data: Intent?) {
        skill = data?.getSerializableExtra(SKILL_INTENT_ARGUMENT) as Skill?
        specialization = data?.getSerializableExtra(SPECIALIZATION_INTENT_ARGUMENT) as Specialization?

        skill?.let { skill ->
            specialization?.let {
                setHandAndFillViews(player.createHand(skill, it))
            } ?: setHandAndFillViews(player.createHand(skill))
        }
        action = null
        specialization = null

        activity?.toast(specialization?.name ?: "rien")

        val handName: String = (skill?.name ?: "") + (specialization?.let { " : ${it.name}" } ?: "")
        current_hand_name.text = handName
    }

    private fun changeStance(newStanceValue: Int) {
        currentStanceTextView.text = Math.abs(newStanceValue).toString()
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