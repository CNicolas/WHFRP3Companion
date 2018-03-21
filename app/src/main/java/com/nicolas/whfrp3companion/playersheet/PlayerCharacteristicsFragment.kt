package com.nicolas.whfrp3companion.playersheet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.whfrp3companion.PLAYER_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.characteristics.PlayerCharacteristicsFragmentViewHolder
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player

class PlayerCharacteristicsFragment : Fragment() {
    internal lateinit var views: PlayerCharacteristicsFragmentViewHolder

    private lateinit var playerFacade: PlayerFacade
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_characteristics, container, false)

        playerFacade = PlayerFacade(context!!)

        views = PlayerCharacteristicsFragmentViewHolder(resultingView)

        if (arguments != null) {
            if (arguments!!.getSerializable(PLAYER_INTENT_ARGUMENT) is Player) {
                player = arguments!!.getSerializable(PLAYER_INTENT_ARGUMENT) as Player
                views.fillViews(player)
            }
        }

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        playerFacade.update(views.getPlayer())

        views.destroy()
    }

    companion object {
        fun newInstance(player: Player): PlayerCharacteristicsFragment {
            val args = Bundle()
            args.putSerializable(PLAYER_INTENT_ARGUMENT, player)

            val fragment = PlayerCharacteristicsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}