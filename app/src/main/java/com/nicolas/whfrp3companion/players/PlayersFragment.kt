package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.nicolas.whfrp3companion.R
import warhammer.database.entities.player.Player

class PlayersFragment : Fragment() {

    lateinit var playersListView: ListView

    private lateinit var players: List<Player>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.players, container, false)
//        val resultingView = inflater.inflate(R.layout.players, container, false)
//
//        val playersListView = resultingView.findViewById(R.id.list_players) as ListView
//
//        players = WarHammerContext.playerFacade.findAll()
//        val playersAdapter = PlayersAdapter(context!!, players)
//        playersListView.adapter = playersAdapter
//        playersListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            onPlayerClick(position)
//        }
//
//        return resultingView
    }

    fun onPlayerClick(position: Int) {
        Toast.makeText(context, players[position].name, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): PlayersFragment {
            val args = Bundle()
            val fragment = PlayersFragment()
            fragment.arguments = args

            return fragment
        }
    }
}