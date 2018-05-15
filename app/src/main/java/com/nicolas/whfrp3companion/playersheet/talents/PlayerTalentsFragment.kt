package com.nicolas.whfrp3companion.playersheet.talents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_TYPE_TAG
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.TalentsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.TalentSearchDialog
import org.koin.android.ext.android.inject

class PlayerTalentsFragment : Fragment() {
    @BindView(R.id.talentsRecyclerView)
    lateinit var talentsRecyclerView: RecyclerView

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

        talentsRecyclerView.layoutManager = LinearLayoutManager(activity)
        talentsRecyclerView.adapter = TalentsAdapter(activity!!, player.talents)

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.search)
    fun openTalentSearchDialog() {
        val talentSearchDialog = TalentSearchDialog.newInstance(true)
        talentSearchDialog.show(activity?.supportFragmentManager, DIALOG_TALENT_TYPE_TAG)
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