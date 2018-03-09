package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.*
import com.nicolas.whfrp3companion.R

class PlayersFragment : Fragment() {
    @BindView(R.id.list_players)
    lateinit var playersListView: ListView
    @BindView(R.id.new_player_edit_text)
    lateinit var newPlayerEditText: EditText
    @BindView(R.id.new_player_button)
    lateinit var newPlayerButton: ImageButton

    //    private lateinit var players: List<Player>
    private lateinit var players: List<String>

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.players, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        players = listOf()
//        updatePlayers()

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnItemClick(R.id.list_players)
    fun onPlayerClick(position: Int) {
        Toast.makeText(context, "CLICKED : " + players[position], Toast.LENGTH_SHORT).show()
    }

    @OnTextChanged(R.id.new_player_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun playerNameChange(editable: Editable) {
        newPlayerButton.visibility = if (!editable.isBlank()) View.VISIBLE else View.GONE
    }

    @OnClick(R.id.new_player_button)
    fun createNewPlayer() {
//        WarHammerContext.playerFacade.add(Player(newPlayerEditText.text.toString()))
        if (!newPlayerEditText.text.isNullOrBlank()) {
            updatePlayers()
        }
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