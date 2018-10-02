package com.nicolas.whfrp3companion.playersheet.state

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.*
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.content_stance_bar.*
import kotlinx.android.synthetic.main.fragment_player_state.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject
import kotlin.math.abs

class PlayerStateFragment : Fragment() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var playerName: String
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_state, container, false)

        playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        return resultingView
    }

    override fun onResume() {
        setupViews()
        setupViewsEvents()

        super.onResume()
    }

    private fun setupViews() {
        doAsync {
            player = playerRepository.find(playerName)!!

            uiThread {
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
            }
        }
    }

    private fun setupViewsEvents() {
        removeWoundButton.setOnClickListener { removeWound() }
        addWoundButton.setOnClickListener { addWound() }

        removeStressButton.setOnClickListener { removeStress() }
        addStressButton.setOnClickListener { addStress() }

        removeExhaustionButton.setOnClickListener { removeExhaustion() }
        addExhaustionButton.setOnClickListener { addExhaustion() }
    }

    private fun removeWound() {
        player.heal(1)
        removeWoundButton.isEnabled = player.wounds > 0

        updateWoundsText()

        updatePlayerAsync()
    }

    private fun addWound() {
        player.loseHealth(1)
        removeWoundButton.isEnabled = true

        updateWoundsText()

        updatePlayerAsync()
    }

    private fun removeStress() {
        player.removeStress(1)
        removeStressButton.isEnabled = player.stress > 0

        updateStressText()

        updatePlayerAsync()
    }

    private fun addStress() {
        player.addStress(1)
        removeStressButton.isEnabled = true

        updateStressText()

        updatePlayerAsync()
    }

    private fun removeExhaustion() {
        player.removeExhaustion(1)
        removeExhaustionButton.isEnabled = player.exhaustion > 0

        updateExhaustionText()

        updatePlayerAsync()
    }

    private fun addExhaustion() {
        player.addExhaustion(1)
        removeExhaustionButton.isEnabled = true

        updateExhaustionText()

        updatePlayerAsync()
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

        val stanceChangeListener = StanceChangeListener(context!!,
                { current_stance.setTextColor(it) },
                { changeStance(it) })
        stanceBar.setOnProgressChangeListener(stanceChangeListener)

        stanceBar.progress = player.stance
        stanceBar.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
            override fun transform(value: Int): Int = abs(value)
        }
    }

    private fun changeStance(newStanceValue: Int) {
        current_stance.text = Math.abs(newStanceValue).toString()
        player.stance = newStanceValue

        updatePlayerAsync()
    }

    private fun updatePlayerAsync() {
        doAsync {
            player = playerRepository.update(player)
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