package com.nicolas.whfrp3companion.playersheet.state

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import kotlin.math.abs

class PlayerStateFragment : Fragment() {
    @BindView(R.id.remove_wound)
    lateinit var removeWoundView: Button
    @BindView(R.id.wounds)
    lateinit var woundsView: TextView
    @BindView(R.id.stance)
    lateinit var stanceView: DiscreteSeekBar

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

        stanceView.min = -player.maxConservative
        stanceView.max = player.maxReckless
        stanceView.setOnProgressChangeListener(StanceChangeListener(context!!, player))
        stanceView.progress = player.stance
        stanceView.numericTransformer = object : DiscreteSeekBar.NumericTransformer() {
            override fun transform(value: Int): Int = abs(value)

        }

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.remove_wound)
    fun removeWound() {
        if (player.wounds > 0) {
            removeWoundView.isEnabled = true
            player.wounds -= 1
        } else {
            removeWoundView.isEnabled = false
        }

        updateWoundsText()

        doAsync {
            playerFacade.update(player)
        }
    }

    @OnClick(R.id.add_wound)
    fun addWound() {
        player.wounds += 1

        updateWoundsText()

        doAsync {
            playerFacade.update(player)
        }
    }

    private fun updateWoundsText() {
        woundsView.text = "${player.wounds} / ${player.maxWounds}"
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