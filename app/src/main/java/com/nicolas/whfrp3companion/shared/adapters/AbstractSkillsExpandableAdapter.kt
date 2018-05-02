package com.nicolas.whfrp3companion.shared.adapters

import android.widget.BaseExpandableListAdapter
import com.nicolas.models.player.playerLinked.skill.Skill

abstract class AbstractSkillsExpandableAdapter(protected val skills: List<Skill>) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int) = skills[groupPosition]
    override fun getGroupCount() = skills.size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChild(groupPosition: Int, childPosition: Int) = getGroup(groupPosition).specializations[childPosition]
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int) = getGroup(groupPosition).specializations.size

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}