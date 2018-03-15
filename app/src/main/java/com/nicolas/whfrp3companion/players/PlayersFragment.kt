package com.nicolas.whfrp3companion.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import butterknife.*
import com.nicolas.whfrp3companion.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.PlayerSheetActivity
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText

class PlayersFragment : Fragment() {
    @BindView(R.id.list_players)
    lateinit var playersListView: ListView

    private lateinit var playerFacade: PlayerFacade
    private lateinit var players: List<Player>
    private lateinit var unbinder: Unbinder

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

    @OnClick(R.id.fab_new_player)
    fun createNewPlayer() {
        activity?.alert {
            title = getString(R.string.new_player)
            customView {
                val name = textInputEditText()

                yesButton {
                    if (!name.text.isNullOrBlank()) {
                        doAsync {
                            playerFacade.add(Player(name.text.toString()))
                            updatePlayers()
                        }
                    }
                }
                noButton {}
            }
        }?.show()
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