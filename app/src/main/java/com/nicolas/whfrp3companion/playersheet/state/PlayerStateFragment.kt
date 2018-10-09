package com.nicolas.whfrp3companion.playersheet.state

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.createTextWatcher
import com.nicolas.whfrp3companion.shared.viewModifications.intValue
import kotlinx.android.synthetic.main.fragment_player_state.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

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
                max_wounds.text = "${player.maxWounds}"
                max_exhaustion.text = "${player.maxExhaustion}"
                max_stress.text = "${player.maxStress}"

                defense.text = "${player.defense}"
                soak.text = "${player.soak}"
                toughness.text = "${player.toughness.value}"

                wounds.setText("${player.wounds}")
                exhaustion.setText("${player.exhaustion}")
                stress.setText("${player.stress}")
            }
        }
    }

    private fun setupViewsEvents() {
        wounds.addTextChangedListener(createTextWatcher { _ -> save() })
        exhaustion.addTextChangedListener(createTextWatcher { _ -> save() })
        stress.addTextChangedListener(createTextWatcher { _ -> save() })
    }

    private fun save() {
        player.wounds = wounds.intValue
        player.exhaustion = exhaustion.intValue
        player.stress = stress.intValue

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