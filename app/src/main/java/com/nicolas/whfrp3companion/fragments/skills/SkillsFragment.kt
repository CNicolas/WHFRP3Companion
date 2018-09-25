package com.nicolas.whfrp3companion.fragments.skills

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.loadSkills
import com.nicolas.whfrp3companion.R
import kotlinx.android.synthetic.main.fragment_skills.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SkillsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_skills, container, false)

        doAsync {
            val skills = loadSkills(context!!)

            uiThread {
                val skillsAdapter = SkillsExpandableAdapter(context!!, skills)
                skillsList.setAdapter(skillsAdapter)

                add_skill.visibility = View.GONE
            }
        }

        return resultingView
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