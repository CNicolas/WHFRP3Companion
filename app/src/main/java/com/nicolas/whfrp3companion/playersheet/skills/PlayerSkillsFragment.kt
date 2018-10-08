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
import com.nicolas.models.extensions.advanced
import com.nicolas.models.player.Player
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.advancedDiceRoller.PlayerAdvancedDiceRollerFragment
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_skills.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.selector
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerSkillsFragment : Fragment(), SkillListener {
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
                skills_listview.setGroupIndicator(null)
            }
        }

        return resultingView
    }

    override fun skillSecondaryHandler(skill: Skill) {
        goToDiceRollerFragment(skill)
    }

    override fun specializationSecondaryHandler(skill: Skill, specialization: Specialization) {
        goToDiceRollerFragment(skill, specialization)
    }

    private fun setupViewsEvents() {
        add_skill.setOnClickListener {
            activity?.let { safeActivity ->
                doAsync {
                    val advancedSkills = loadSkills(safeActivity).advanced

                    uiThread { _ ->
                        openAddSkillSelector(safeActivity, advancedSkills)
                    }
                }
            }
        }
    }

    private fun openAddSkillSelector(safeActivity: FragmentActivity, skills: List<Skill>) {
        safeActivity.selector(getString(R.string.skill), skills.map { skill -> skill.name }) { _, index ->
            doAsync {
                player = playerRepository.update(player.addSkill(skills[index]))

                uiThread {
                    setSkillsListAdapter()
                }
            }
        }
    }

    private fun setSkillsListAdapter() {
        activity?.let {
            val skillsAdapter = PlayerSkillsExpandableAdapter(it, player, this)
            skills_listview.setAdapter(skillsAdapter)
        }
    }

    private fun goToDiceRollerFragment(skill: Skill, specialization: Specialization? = null) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.playersheet_content_frame, PlayerAdvancedDiceRollerFragment.newInstance(player.name, skill, specialization))
                ?.commit()
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