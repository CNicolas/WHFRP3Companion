package com.nicolas.whfrp3companion.playersheet.talents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.addTalent
import com.nicolas.models.extensions.removeTalent
import com.nicolas.models.extensions.toggleEquipment
import com.nicolas.models.player.Player
import com.nicolas.models.player.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_SEARCH_TAG
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.dialogs.TalentSearchDialog
import kotlinx.android.synthetic.main.fragment_player_talents.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerTalentsFragment : Fragment(), TalentListener {
    private lateinit var unbinder: Unbinder

    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_talents, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        player = playerRepository.find(playerName)!!

        setPlayerTalentsAdapter()

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        setPlayerTalentsAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.search)
    fun openTalentSearchDialog() {
        val talentSearchDialog = TalentSearchDialog.newInstance(player.name)
        talentSearchDialog.show(activity?.supportFragmentManager, DIALOG_TALENT_SEARCH_TAG)
    }

    // region TalentListener
    override fun onAddTalent(talent: Talent) {
        doAsync {
            player.addTalent(talent)
            player = playerRepository.update(player)

            uiThread {
                talentsRecyclerView.adapter = createTalentsAdapter()
            }
        }
    }

    override fun onToggleTalentEquipment(talent: Talent) {
        doAsync {
            player.toggleEquipment(talent)
            player = playerRepository.update(player)

            uiThread {
                talentsRecyclerView.adapter = createTalentsAdapter()
            }
        }
    }

    override fun onRemoveTalent(talent: Talent) {
        doAsync {
            player.removeTalent(talent)
            player = playerRepository.update(player)

            val adapter = PlayerTalentsAdapter(activity!!, player.talents, this@PlayerTalentsFragment, TalentEditionMode.EQUIPABLE_OR_REMOVABLE)
            uiThread {
                talentsRecyclerView.adapter = adapter
            }
        }
    }
    // endregion

    private fun setPlayerTalentsAdapter() {
        doAsync {
            player = playerRepository.find(player.name)!!

            uiThread {
                talentsRecyclerView?.let {
                    talentsRecyclerView.layoutManager = LinearLayoutManager(activity!!)
                    talentsRecyclerView.adapter = createTalentsAdapter()
                }
            }
        }
    }

    private fun createTalentsAdapter(): PlayerTalentsAdapter {
        return PlayerTalentsAdapter(activity!!, player.talents, this, TalentEditionMode.EQUIPABLE_OR_REMOVABLE)
    }

    companion object {
        fun newInstance(playerName: String): PlayerTalentsFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerTalentsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}