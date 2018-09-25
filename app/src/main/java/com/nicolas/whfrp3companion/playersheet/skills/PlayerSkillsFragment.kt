package com.nicolas.whfrp3companion.playersheet.skills

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.PlayerRepository
import com.nicolas.database.loadSkills
import com.nicolas.models.extensions.addSkill
import com.nicolas.models.player.Player
import com.nicolas.models.skill.Skill
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_skills.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.selector
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
                setupViewsEvents()

                setSkillsListAdapter()
                skillsList.setGroupIndicator(null)
            }
        }

        return resultingView
    }

    private fun setupViewsEvents() {
        add_skill.setOnClickListener {
            activity?.let { safeActivity ->
                doAsync {
                    val advancedSkills = loadSkills(safeActivity)

                    uiThread {
                        openAddSkillSelector(safeActivity, advancedSkills)
                    }
                }
            }
        }
    }

    fun openAddSkillSelector(safeActivity: FragmentActivity, advancedSkills: List<Skill>) {
        safeActivity.selector("", advancedSkills.map { skill -> skill.name }) { _, index ->
            doAsync {
                player = playerRepository.update(player.addSkill(advancedSkills[index]))

                uiThread {
                    setSkillsListAdapter()
                }
            }
        }
    }

    private fun setSkillsListAdapter() {
        activity?.let {
            val skillsAdapter = PlayerSkillsExpandableAdapter(it, player)
            skillsList.setAdapter(skillsAdapter)
        }
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