package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.WarHammerContext
import warhammer.database.entities.player.Player

class PlayersFragment : Fragment() {
    lateinit var playersListView: ListView

    private lateinit var players: List<Player>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView = inflater.inflate(R.layout.players, container, false)

        val playersListView = activity.findViewById(R.id.list_players) as ListView

        players = WarHammerContext.playerFacade.findAll()
        val playersAdapter = PlayersAdapter(activity, players)
        playersListView.adapter = playersAdapter
        playersListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(context, players[position].name, Toast.LENGTH_SHORT).show()
        }

        return resultingView
    }

    fun onPlayerClick(position: Int) {
        Toast.makeText(context, players[position].name, Toast.LENGTH_SHORT).show()
    }
}