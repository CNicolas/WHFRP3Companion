package com.nicolas.whfrp3companion.fragments.skills

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.database.loadSkills
import com.nicolas.whfrp3companion.R
import kotlinx.android.synthetic.main.fragment_skills.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SkillsFragment : Fragment() {
    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_skills, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        doAsync {
            val skills = loadSkills(context!!)

            uiThread {
                val skillsAdapter = SkillsExpandableAdapter(context!!, skills)
                skillsList.setAdapter(skillsAdapter)
            }
        }
        
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