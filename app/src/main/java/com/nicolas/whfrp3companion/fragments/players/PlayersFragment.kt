package com.nicolas.whfrp3companion.fragments.players

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.PlayerSheetActivity
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Race
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread

class PlayersFragment : Fragment() {
    @BindView(R.id.list_players)
    lateinit var playersListView: ListView

    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade
    private lateinit var players: List<Player>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_players, container, false)

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
        if (activity != null) {
            startActivity(activity!!.intentFor<PlayerSheetActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to players[position].name
            ))
        }
    }

    @OnItemLongClick(R.id.list_players)
    fun onPlayerLongClick(view: View, position: Int): Boolean {
        val player = players[position]
        val playerPopupMenu = PopupMenu(activity!!, view, Gravity.END)
        playerPopupMenu.menuInflater.inflate(R.menu.list_element_player, playerPopupMenu.menu)
        playerPopupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_player -> {
                    playerFacade.deletePlayer(player)
                    updatePlayers()
                }
            }
            true
        }
        playerPopupMenu.show()

        return true
    }

    @OnClick(R.id.fab_new_player)
    fun createNewPlayer() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_create_player, null, false)

        val playerName: EditText = view.findViewById(R.id.player_name) as EditText
        val raceSpinner: Spinner = view.findViewById(R.id.race) as Spinner
        raceSpinner.adapter = ArrayAdapter(view.context!!, R.layout.element_enum_spinner, Race.values().map { view.context.getString(it.labelId) })

        builder.setView(view)
        builder.setTitle(R.string.create_player)
        builder.setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.dismiss() })
        builder.setPositiveButton(android.R.string.ok, { dialog, _ ->
            playerFacade.add(Player(name = playerName.text.toString(), race = Race[raceSpinner.selectedItemPosition]))
            updatePlayers()
            dialog.dismiss()
        })

        builder.create().show()
    }

    private fun updatePlayers() {
        doAsync {
            players = playerFacade.findAll()
            uiThread {
                playersListView.adapter = PlayersAdapter(context!!, players)
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