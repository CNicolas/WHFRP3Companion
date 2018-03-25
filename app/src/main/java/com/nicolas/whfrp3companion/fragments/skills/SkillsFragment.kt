package com.nicolas.whfrp3companion.fragments.skills

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.components.labelId
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.staticData.loadSkills


class SkillsFragment : Fragment() {
    @BindView(R.id.skills_list)
    lateinit var skillsList: ExpandableListView

    private lateinit var unbinder: Unbinder
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_skills, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val skills = loadSkills(context!!)
        val skillsAdapter = SkillsAdapter(context!!, skills, skills.map { it to it.specializations }.toMap())
        skillsList.setAdapter(skillsAdapter)

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    companion object {
        fun newInstance(): SkillsFragment {
            val args = Bundle()
            val fragment = SkillsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}