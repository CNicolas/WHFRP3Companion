package com.nicolas.whfrp3companion.playersheet.skills

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_skills.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerSkillsFragment : Fragment() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_skills, container, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        doAsync {
            player = playerRepository.find(playerName)!!

            uiThread {
                val skillsAdapter = PlayerSkillsExpandableAdapter(context!!, player)
                skillsList.setAdapter(skillsAdapter)
                skillsList.setGroupIndicator(null)
            }
        }

        return resultingView
    }

    companion object {
        fun newInstance(playerName: String): PlayerSkillsFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerSkillsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}