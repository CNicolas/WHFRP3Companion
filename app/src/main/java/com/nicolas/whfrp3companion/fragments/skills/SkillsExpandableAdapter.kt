package com.nicolas.whfrp3companion.fragments.skills

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.adapters.AbstractSkillsExpandableAdapter
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType.ADVANCED
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType.BASIC
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Specialization

class SkillsExpandableAdapter(private val context: Context,
                              skills: List<Skill>) : AbstractSkillsExpandableAdapter(skills) {
    private val inflater = LayoutInflater.from(context)

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getChildViewHolderOfView(convertView, parent)

        val specialization = getChild(groupPosition, childPosition)

        holder.skill = getGroup(groupPosition)
        holder.specialization = specialization

        holder.specializationNameView.text = specialization.name

        return resultingView
    }

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getGroupViewHolderOfView(convertView, parent)

        val skill = getGroup(groupPosition)

        holder.skill = skill

        holder.skillNameView.text = skill.name
        when (skill.type) {
            BASIC -> holder.skillNameView.setTypeface(null, Typeface.BOLD)
            ADVANCED -> holder.skillNameView.setTypeface(null, Typeface.BOLD_ITALIC)
        }
        holder.characteristicView.text = context.getString(skill.characteristic.labelId).substring(0..2)

        return resultingView
    }

    private fun getChildViewHolderOfView(savedView: View?, parent: ViewGroup?): Pair<View, ChildViewHolder> {
        var view = savedView

        val holder: ChildViewHolder
        if (view != null) {
            holder = view.tag as ChildViewHolder
        } else {
            view = inflater.inflate(R.layout.list_skills_child, parent, false)
            holder = ChildViewHolder(view)
            view!!.tag = holder
        }

        return Pair(view, holder)
    }

    private fun getGroupViewHolderOfView(savedView: View?, parent: ViewGroup?): Pair<View, GroupViewHolder> {
        var view = savedView

        val holder: GroupViewHolder
        if (view != null) {
            holder = view.tag as GroupViewHolder
        } else {
            view = inflater.inflate(R.layout.list_skills_header, parent, false)
            holder = GroupViewHolder(view)
            view!!.tag = holder
        }

        return Pair(view, holder)
    }

    internal class ChildViewHolder(view: View) {
        @BindView(R.id.specialization_name)
        lateinit var specializationNameView: TextView

        lateinit var skill: Skill
        lateinit var specialization: Specialization

        init {
            ButterKnife.bind(this, view)
        }
    }

    internal class GroupViewHolder(view: View) {
        @BindView(R.id.skill_name)
        lateinit var skillNameView: TextView
        @BindView(R.id.characteristic)
        lateinit var characteristicView: TextView

        lateinit var skill: Skill

        init {
            ButterKnife.bind(this, view)
        }
    }
}