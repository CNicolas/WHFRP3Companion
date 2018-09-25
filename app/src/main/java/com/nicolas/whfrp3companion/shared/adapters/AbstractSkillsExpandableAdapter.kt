package com.nicolas.whfrp3companion.shared.adapters

import android.widget.BaseExpandableListAdapter
import com.nicolas.models.skill.Skill
import java.text.Normalizer

abstract class AbstractSkillsExpandableAdapter(skills: List<Skill>) : BaseExpandableListAdapter() {
    protected val skills: List<Skill> = skills.sortedBy { Normalizer.normalize(it.name, Normalizer.Form.NFD) }

    override fun getGroup(groupPosition: Int) = skills[groupPosition]
    override fun getGroupCount() = skills.size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChild(groupPosition: Int, childPosition: Int) = getGroup(groupPosition).specializations[childPosition]
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int) = getGroup(groupPosition).specializations.size

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}