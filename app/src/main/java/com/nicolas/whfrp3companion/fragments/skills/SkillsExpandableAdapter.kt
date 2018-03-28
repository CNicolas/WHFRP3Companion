package com.nicolas.whfrp3companion.fragments.skills

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Specialization
import org.jetbrains.anko.toast

class SkillsExpandableAdapter(private val context: Context,
                              private val headers: List<Skill>,
                              private val children: Map<Skill, List<Specialization>>) : BaseExpandableListAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getChild(groupPosition: Int, childPosition: Int): Specialization {
        val skill = getGroup(groupPosition)
        val specialization = skill.specializations[childPosition]

        Log.i("SKILL", "skill : ${skill.name}, specialization : $specialization")

        return specialization
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int): Int = children[headers[groupPosition]]?.size ?: 0

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val specialization = getChild(groupPosition, childPosition)

        var resultingView = convertView
        val holder: ChildViewHolder
        if (resultingView != null) {
            holder = resultingView.tag as ChildViewHolder
        } else {
            resultingView = inflater.inflate(R.layout.list_skills_child, parent, false)
            holder = ChildViewHolder(resultingView)
            resultingView!!.tag = holder
        }

        holder.specialization = specialization
        holder.specializationNameView.text = specialization.name
        holder.specializationNameView.isChecked = specialization.isSpecialized
        holder.specializationNameView.isFocusable = false

        return resultingView
    }

    //region Group
    override fun getGroup(groupPosition: Int): Skill = headers[groupPosition]

    override fun getGroupCount(): Int = headers.size
    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val skill = getGroup(groupPosition)

        var resultingView = convertView
        val holder: GroupViewHolder
        if (resultingView != null) {
            holder = resultingView.tag as GroupViewHolder
        } else {
            resultingView = inflater.inflate(R.layout.list_skills_header, parent, false)
            holder = GroupViewHolder(resultingView)
            resultingView!!.tag = holder
        }

        holder.skill = skill
        holder.skillNameView.text = skill.name
        holder.characteristicView.text = context.getString(skill.characteristic.labelId).substring(0..2)

        when (skill.level) {
            1 -> {
                holder.formationLevelsView.check(holder.level1View.id)
            }
            2 -> {
                holder.formationLevelsView.check(holder.level2View.id)
            }
            3 -> {
                holder.formationLevelsView.check(holder.level3View.id)
            }
            else -> {
                holder.formationLevelsView.clearCheck()
            }
        }

        holder.level1View.onFormationLevelClicked(1, skill)
        holder.level2View.onFormationLevelClicked(2, skill)
        holder.level3View.onFormationLevelClicked(3, skill)

        holder.formationLevelsView.isFocusable = false
        holder.level1View.isFocusable = false
        holder.level2View.isFocusable = false
        holder.level3View.isFocusable = false

        return resultingView
    }

    // endregion

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    private fun RadioButton.onFormationLevelClicked(level: Int, skill: Skill) {
        setOnClickListener { _ ->
            when (level) {
                skill.level -> skill.level = 0
                else -> skill.level = level
            }
            context.toast("${skill.name} - ${skill.level} ($level)")
            isFocusable = false
        }
    }

    internal class GroupViewHolder(private val view: View) {
        @BindView(R.id.skill_name)
        lateinit var skillNameView: TextView
        @BindView(R.id.characteristic)
        lateinit var characteristicView: TextView

        @BindView(R.id.formation_levels)
        lateinit var formationLevelsView: RadioGroup
        @BindView(R.id.level_1)
        lateinit var level1View: RadioButton
        @BindView(R.id.level_2)
        lateinit var level2View: RadioButton
        @BindView(R.id.level_3)
        lateinit var level3View: RadioButton

        lateinit var skill: Skill

        init {
            ButterKnife.bind(this, view)
        }
    }

    internal class ChildViewHolder(private val view: View) {
        @BindView(R.id.specialization_name)
        lateinit var specializationNameView: CheckBox

        lateinit var specialization: Specialization

        init {
            ButterKnife.bind(this, view)
        }

        @OnClick(R.id.specialization_name)
        fun toggleSpecialization(view: View) {
            specialization.isSpecialized = !specialization.isSpecialized
            (view as CheckBox).isChecked = specialization.isSpecialized
        }
    }
}