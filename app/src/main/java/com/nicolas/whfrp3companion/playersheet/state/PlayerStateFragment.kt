package com.nicolas.whfrp3companion.playersheet.state

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.*
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_player_state.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import kotlin.math.abs

class PlayerStateFragment : Fragment() {
    private lateinit var unbinder: Unbinder

    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_state, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        player = playerRepository.find(playerName)!!

        removeWoundButton.isEnabled = player.wounds > 0
        updateWoundsText()

        removeStressButton.isEnabled = player.stress > 0
        updateStressText()

        removeExhaustionButton.isEnabled = player.exhaustion > 0
        updateExhaustionText()

        setupStance()

        defenseTextView.text = "${player.defense}"
        soakTextView.text = "${player.soak}"

        weaponsListView.adapter = WeaponsAdapter(context!!, player.getEquippedWeapons())

        setupEncumbrance()

        setupMoney()

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.removeWoundButton)
    fun removeWound() {
        player.heal(1)
        removeWoundButton.isEnabled = player.wounds > 0

        updateWoundsText()

        updatePlayerAsync()
    }

    @OnClick(R.id.add_wound)
    fun addWound() {
        player.loseHealth(1)
        removeWoundButton.isEnabled = true

        updateWoundsText()

        updatePlayerAsync()
    }

    @OnClick(R.id.removeStressButton)
    fun removeStress() {
        player.removeStress(1)
        removeStressButton.isEnabled = player.stress > 0

        updateStressText()

        updatePlayerAsync()
    }

    @OnClick(R.id.add_stress)
    fun addStress() {
        player.addStress(1)
        removeStressButton.isEnabled = true

        updateStressText()

        updatePlayerAsync()
    }

    @OnClick(R.id.removeExhaustionButton)
    fun removeExhaustion() {
        player.removeExhaustion(1)
        removeExhaustionButton.isEnabled = player.exhaustion > 0

        updateExhaustionText()

        updatePlayerAsync()
    }

    @OnClick(R.id.add_exhaustion)
    fun addExhaustion() {
        player.addExhaustion(1)
        removeExhaustionButton.isEnabled = true

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
        woundsTextView.text = "${player.wounds} / ${player.maxWounds}"
    }

    private fun updateStressText() {
        stressTextView.text = "${player.stress}"
    }

    private fun updateExhaustionText() {
        exhaustionTextView.text = "${player.exhaustion}"
    }

    private fun setupStance() {
        stanceBar.min = -player.maxConservative
        stanceBar.max = player.maxReckless
        stanceBar.setOnProgressChangeListener(StanceChangeListener(context!!, player, currentStanceTextView))
        stanceBar.progress = player.stance
        stanceBar.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
            override fun transform(value: Int): Int = abs(value)
        }
    }

    private fun setupEncumbrance() {
        encumbranceBar.min = 0
        encumbranceBar.max = player.maxEncumbrance
        encumbranceBar.progress = player.encumbrance
        encumbranceBar.isEnabled = false

        val colorId = when {
            player.encumbrance < player.encumbranceOverload -> R.color.conservative
            player.encumbrance < player.maxEncumbrance -> R.color.orange
            else -> R.color.reckless
        }
        val color = ContextCompat.getColor(context!!, colorId)
        val colorStateList = ColorStateList.valueOf(color)
        encumbranceBar.setScrubberColor(colorStateList)

        encumbranceTextView.text = "${player.encumbrance} / ${player.maxEncumbrance}"
        encumbranceTextView.setTextColor(colorStateList)
    }

    private fun setupMoney() {
        goldTextView.text = "${player.gold}"
        silverTextView.text = "${player.silver}"
        brassTextView.text = "${player.brass}"
    }

    private fun updatePlayerAsync() {
        doAsync {
            playerRepository.update(player)
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