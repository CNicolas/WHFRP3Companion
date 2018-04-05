package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player

class PlayerInventoryFragment : Fragment() {
    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_inventory, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        playerFacade = PlayerFacade(context!!)
        player = playerFacade.find(playerName)!!

        return resultingView
    }

    companion object {
        fun newInstance(playerName: String): PlayerInventoryFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerInventoryFragment()
            fragment.arguments = args

            return fragment
        }
    }
}