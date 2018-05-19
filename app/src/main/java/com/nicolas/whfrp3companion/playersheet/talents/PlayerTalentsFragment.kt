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
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_TYPE_TAG
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

        doAsync {
            val adapter = createTalentsAdapter()

            uiThread {
                talentsRecyclerView.layoutManager = LinearLayoutManager(activity!!)
                talentsRecyclerView.adapter = adapter
            }
        }

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.search)
    fun openTalentSearchDialog() {
        val talentSearchDialog = TalentSearchDialog.newInstance(TalentEditionMode.ADDABLE)
        talentSearchDialog.show(activity?.supportFragmentManager, DIALOG_TALENT_TYPE_TAG)
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

            val adapter = PlayerTalentsAdapter(activity!!, player.talents, TalentEditionMode.EQUIPABLE_OR_REMOVABLE)
            uiThread {
                talentsRecyclerView.adapter = adapter
            }
        }
    }
    // endregion

    private fun createTalentsAdapter(): PlayerTalentsAdapter {
        val adapter = PlayerTalentsAdapter(activity!!, player.talents, TalentEditionMode.EQUIPABLE_OR_REMOVABLE)
        adapter.addTalentListener(this)

        return adapter
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