package com.nicolas.whfrp3companion.playersheet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.Player
import org.jetbrains.anko.toast

class PlayerCharacteristicsFragment : Fragment() {
    private lateinit var player: Player

    @BindView(R.id.player_name)
    lateinit var playerNameTextView: TextView

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_characteristics, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        playerNameTextView.text = "No player ?"

        if (arguments != null) {
            if (arguments!!.getSerializable(PLAYER_NAME_INTENT_ARGUMENT) is Player) {
                player = arguments!!.getSerializable(PLAYER_NAME_INTENT_ARGUMENT) as Player
                playerNameTextView.text = player.name
            }
        }

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    companion object {
        fun newInstance(player: Player): PlayerCharacteristicsFragment {
            val args = Bundle()
            args.putSerializable(PLAYER_NAME_INTENT_ARGUMENT, player)

            val fragment = PlayerCharacteristicsFragment()
            fragment.arguments = args

            fragment.activity?.toast("I got this : ${player.name}")

            return fragment
        }
    }
}