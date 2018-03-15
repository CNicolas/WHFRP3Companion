package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import butterknife.*
import com.nicolas.whfrp3companion.PLAYER_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.PlayerSheetActivity
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread

class PlayersFragment : Fragment() {
    @BindView(R.id.list_players)
    lateinit var playersListView: ListView
    @BindView(R.id.new_player_edit_text)
    lateinit var newPlayerEditText: EditText
    @BindView(R.id.new_player_button)
    lateinit var newPlayerButton: ImageButton

    private lateinit var playerFacade: PlayerFacade
    private lateinit var players: List<Player>
    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.players, container, false)

        unbinder = ButterKnife.bind(this, resultingView)
        playerFacade = PlayerFacade(context!!)

        updatePlayers()

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnItemClick(R.id.list_players)
    fun onPlayerClick(position: Int) {
        startActivity(activity?.intentFor<PlayerSheetActivity>(
                PLAYER_INTENT_ARGUMENT to players[position]
        ))
    }

    @OnTextChanged(R.id.new_player_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun playerNameChange(editable: Editable) {
        newPlayerButton.visibility = if (!editable.isBlank()) View.VISIBLE else View.GONE
    }

    @OnClick(R.id.new_player_button)
    fun createNewPlayer() {
        if (!newPlayerEditText.text.isNullOrBlank()) {
            doAsync {
                playerFacade.add(Player(newPlayerEditText.text.toString()))
                updatePlayers()
            }
        }
    }

    private fun updatePlayers() {
        doAsync {
            players = playerFacade.findAll()
            uiThread {
                playersListView.adapter = PlayersAdapter(context!!, players)
                newPlayerEditText.text.clear()
            }
        }
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