package com.nicolas.whfrp3companion.fragments.players

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import butterknife.*
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Race
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.PlayerSheetActivity
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.enums.labelId
import kotlinx.android.synthetic.main.fragment_players.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayersFragment : Fragment() {
    private lateinit var unbinder: Unbinder

    private val playerRepository by inject<PlayerRepository>()

    private lateinit var players: List<Player>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_players, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        updatePlayers()

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnItemClick(R.id.playersList)
    fun onPlayerClick(position: Int) {
        if (activity != null) {
            startActivity(activity!!.intentFor<PlayerSheetActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to players[position].name
            ))
        }
    }

    @OnItemLongClick(R.id.playersList)
    fun onPlayerLongClick(view: View, position: Int): Boolean {
        val player = players[position]
        val playerPopupMenu = PopupMenu(activity!!, view, Gravity.END)
        playerPopupMenu.menuInflater.inflate(R.menu.list_element_player, playerPopupMenu.menu)
        playerPopupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_player -> {
                    playerRepository.delete(player)
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

        val playerNameView: EditText = view.findViewById(R.id.player_name) as EditText
        val raceView: Spinner = view.findViewById(R.id.race) as Spinner
        raceView.adapter = ArrayAdapter(view.context!!, R.layout.element_enum_spinner, Race.values().map { view.context.getString(it.labelId) })

        builder.setView(view)
        builder.setTitle(R.string.create_player)
        builder.setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.dismiss() })
        builder.setPositiveButton(android.R.string.ok, { dialog, _ ->
            if (playerNameView.text.trim() != "") {
                playerRepository.add(Player(name = playerNameView.text.toString(), race = Race[raceView.selectedItemPosition]))
                updatePlayers()
                dialog.dismiss()
            } else {
                context?.toast("No name entered")
            }
        })

        builder.create().show()
    }

    private fun updatePlayers() {
        doAsync {
            players = playerRepository.findAll()
            uiThread {
                playersList.adapter = PlayersAdapter(context!!, players)
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