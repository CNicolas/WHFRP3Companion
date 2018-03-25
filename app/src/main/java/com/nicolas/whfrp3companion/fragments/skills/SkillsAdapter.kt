package com.nicolas.whfrp3companion.fragments.skills

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.components.labelId
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Specialization

class SkillsAdapter(private val context: Context,
                    private val headers: List<Skill>,
                    private val children: Map<Skill, List<Specialization>>) : BaseExpandableListAdapter() {
    override fun getChild(groupPosition: Int, childPosition: Int): Specialization =
            children[headers[groupPosition]]!![childPosition]

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int): Int = children[headers[groupPosition]]?.size ?: 0

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val specialization = getChild(groupPosition, childPosition)

        val resultingView = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.list_skills_child, null)
        } else {
            convertView
        }

        resultingView.findViewById<TextView>(R.id.child).text = specialization.name

        return resultingView
    }

    override fun getGroup(groupPosition: Int): Skill = headers[groupPosition]
    override fun getGroupCount(): Int = headers.size
    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val skill = getGroup(groupPosition)

        val resultingView = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.list_skills_header, null)
        } else {
            convertView
        }

        val headerView = resultingView.findViewById<TextView>(R.id.header)
        headerView.text = skill.name
        headerView.setTypeface(null, Typeface.BOLD)

        val characteristicView = resultingView.findViewById<TextView>(R.id.characteristic)
        characteristicView.text = context.getString(skill.characteristic.labelId)

        return resultingView
    }

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}