package com.nicolas.whfrp3companion.playersheet.state

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.playersheet.extensions.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import kotlin.math.abs

class PlayerStateFragment : Fragment() {
    @BindView(R.id.remove_wound)
    lateinit var removeWoundView: Button
    @BindView(R.id.wounds)
    lateinit var woundsView: TextView

    @BindView(R.id.remove_stress)
    lateinit var removeStressView: Button
    @BindView(R.id.stress)
    lateinit var stressView: TextView

    @BindView(R.id.remove_exhaustion)
    lateinit var removeExhaustionView: Button
    @BindView(R.id.exhaustion)
    lateinit var exhaustionView: TextView

    @BindView(R.id.current_stance)
    lateinit var currentStanceView: TextView
    @BindView(R.id.stance)
    lateinit var stanceView: DiscreteSeekBar

    @BindView(R.id.defense)
    lateinit var defenseView: TextView
    @BindView(R.id.soak)
    lateinit var soakView: TextView

    @BindView(R.id.encumbrance_bar)
    lateinit var encumbranceBarView: SeekBar
    @BindView(R.id.encumbrance_label)
    lateinit var encumbranceLabel: TextView

    @BindView(R.id.gold)
    lateinit var goldView: TextView
    @BindView(R.id.silver)
    lateinit var silverView: TextView
    @BindView(R.id.brass)
    lateinit var brassView: TextView

    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_state, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        playerFacade = PlayerFacade(context!!)
        player = playerFacade.find(playerName)!!

        removeWoundView.isEnabled = player.wounds > 0
        updateWoundsText()

        removeStressView.isEnabled = player.stress > 0
        updateStressText()

        removeExhaustionView.isEnabled = player.exhaustion > 0
        updateExhaustionText()

        setupStance()

        defenseView.text = "${player.defense}"
        soakView.text = "${player.soak}"

        encumbranceBarView.max = player.maxEncumbrance
        encumbranceBarView.progress = player.encumbrance
        encumbranceBarView.isEnabled = false
        encumbranceLabel.text = "${player.encumbrance} / ${player.maxEncumbrance}"

        setupMoney()

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.remove_wound)
    fun removeWound() {
        player.heal(1)
        removeWoundView.isEnabled = player.wounds > 0

        updateWoundsText()

        updatePlayerAsync()
    }

    @OnClick(R.id.add_wound)
    fun addWound() {
        player.loseHealth(1)
        removeWoundView.isEnabled = true

        updateWoundsText()

        updatePlayerAsync()
    }

    @OnClick(R.id.remove_stress)
    fun removeStress() {
        player.removeStress(1)
        removeStressView.isEnabled = player.stress > 0

        updateStressText()

        updatePlayerAsync()
    }

    @OnClick(R.id.add_stress)
    fun addStress() {
        player.addStress(1)
        removeStressView.isEnabled = true

        updateStressText()

        updatePlayerAsync()
    }

    @OnClick(R.id.remove_exhaustion)
    fun removeExhaustion() {
        player.removeExhaustion(1)
        removeExhaustionView.isEnabled = player.exhaustion > 0

        updateExhaustionText()

        updatePlayerAsync()
    }

    @OnClick(R.id.add_exhaustion)
    fun addExhaustion() {
        player.addExhaustion(1)
        removeExhaustionView.isEnabled = true

        updateExhaustionText()

        updatePlayerAsync()
    }

    @OnClick(R.id.change_money)
    fun changeMoney() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_money, null, false)

        val goldPicker = view.findViewById(R.id.gold) as NumberPicker
        val silverPicker = view.findViewById(R.id.silver) as NumberPicker
        val brassPicker = view.findViewById(R.id.brass) as NumberPicker

        builder.setView(view)
        builder.setTitle(R.string.change_money)

        builder.setNegativeButton(R.string.remove, { dialog, _ ->
            try {
                player.removeMoney(goldPicker.value, silverPicker.value, brassPicker.value)
                dialog.dismiss()
            } catch (exception: IllegalArgumentException) {
                context?.toast(R.string.not_enough_money)
            }
        })

        builder.setPositiveButton(R.string.add, { dialog, _ ->
            player.addMoney(goldPicker.value, silverPicker.value, brassPicker.value)
            dialog.dismiss()
        })

        builder.setOnDismissListener {
            updatePlayerAsync()
            setupMoney()
        }

        builder.create().show()
    }

    private fun updateWoundsText() {
        woundsView.text = "${player.wounds} / ${player.maxWounds}"
    }

    private fun updateStressText() {
        stressView.text = "${player.stress}"
    }

    private fun updateExhaustionText() {
        exhaustionView.text = "${player.exhaustion}"
    }

    private fun setupStance() {
        stanceView.min = -player.maxConservative
        stanceView.max = player.maxReckless
        stanceView.setOnProgressChangeListener(StanceChangeListener(context!!, player, currentStanceView))
        stanceView.progress = player.stance
        stanceView.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
            override fun transform(value: Int): Int = abs(value)
        }
    }

    private fun setupMoney() {
        goldView.text = "${player.gold}"
        silverView.text = "${player.silver}"
        brassView.text = "${player.brass}"
    }

    private fun updatePlayerAsync() {
        doAsync {
            playerFacade.update(player)
        }
    }

    companion object {
        fun newInstance(playerName: String): PlayerStateFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerStateFragment()
            fragment.arguments = args

            return fragment
        }
    }
}