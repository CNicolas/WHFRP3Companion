package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import butterknife.*
import com.nicolas.whfrp3companion.R

class PlayersFragment : Fragment() {
    @BindView(R.id.list_players)
    lateinit var playersListView: ListView
    @BindView(R.id.new_player_edit_text)
    lateinit var newPlayerEditText: EditText

    //    private lateinit var players: List<Player>
    private lateinit var players: List<String>

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.players, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        updatePlayers()

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnItemClick(R.id.list_players)
    fun onItemSelected(position: Int) {
        Toast.makeText(context, "CLICKED : " + players[position], Toast.LENGTH_SHORT).show()
    }

    @OnClick(R.id.button_new_player)
    fun createNewPlayer() {
//        WarHammerContext.playerFacade.add(Player(newPlayerEditText.text.toString()))
        updatePlayers()
    }

    private fun updatePlayers() {
//        players = WarHammerContext.playerFacade.findAll()
//        val playersAdapter = PlayersAdapter(context!!, players)
//        playersListView.adapter = playersAdapter
        val newList = players.toMutableList()
        newList.add(newPlayerEditText.text.toString())
        players = newList
        playersListView.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, players)

        newPlayerEditText.text.clear()
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