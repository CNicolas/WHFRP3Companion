package com.nicolas.whfrp3companion.playersheet.actions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_player_actions.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerActionsFragment : Fragment(), ActionListener {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_actions, container, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        player = playerRepository.find(playerName)!!

        setPlayerActionsAdapter()

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        setPlayerActionsAdapter()
    }

    private fun setPlayerActionsAdapter() {
        doAsync {
            player = playerRepository.find(player.name)!!

            uiThread {
                actionsRecyclerView?.let {
                    actionsRecyclerView.layoutManager = LinearLayoutManager(activity!!)
                    actionsRecyclerView.adapter = createActionsAdapter()
                }

                activity?.longToast("${player.actions.map { it.name }}")
            }
        }
    }

    private fun createActionsAdapter(): PlayerActionsAdapter {
        return PlayerActionsAdapter(activity!!, player.actions, this)
    }

    companion object {
        fun newInstance(playerName: String): PlayerActionsFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerActionsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}