package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import warhammer.database.entities.player.Player

class PlayersFragment : Fragment() {
    @BindView(R.id.list_players)
    lateinit var playersListView: ListView

    private lateinit var players: List<Player>

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.players, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

//        players = WarHammerContext.playerFacade.findAll()
//        val playersAdapter = PlayersAdapter(context!!, players)
//        playersListView.adapter = playersAdapter
        playersListView.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listOf("Player1", "Player2"))

//        playersListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            onPlayerClick(position)
//        }

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

//    @OnItemSelected(R.id.list_players)
//    fun onItemSelected(position: Int) {
//        Toast.makeText(context, players[position].name, Toast.LENGTH_SHORT).show()
//    }

    companion object {
        fun newInstance(): PlayersFragment {
            val args = Bundle()
            val fragment = PlayersFragment()
            fragment.arguments = args

            return fragment
        }
    }
}