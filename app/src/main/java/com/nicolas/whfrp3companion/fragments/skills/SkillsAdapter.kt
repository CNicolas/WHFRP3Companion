package com.nicolas.whfrp3companion.fragments.skills

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill

class SkillsAdapter(private val context: Context,
                    private val headers: List<String>,
                    private val children: Map<String, List<Skill>>) : BaseExpandableListAdapter() {
    override fun getChild(groupPosition: Int, childPosition: Int): Skill =
            children[headers[groupPosition]]!![childPosition]

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int): Int = children[headers[groupPosition]]?.size ?: 0

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val child = getChild(groupPosition, childPosition)

        val resultingView = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.list_skills_skill, null)
        } else {
            convertView
        }

        resultingView.findViewById<TextView>(R.id.skill_name).text = child.name

        return resultingView
    }


    override fun getGroup(groupPosition: Int): String = headers[groupPosition]
    override fun getGroupCount(): Int = headers.size
    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val headerText = getGroup(groupPosition)

        val resultingView = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.list_skills_characteristic_header, null)
        } else {
            convertView
        }

        val headerView = resultingView.findViewById<TextView>(R.id.characteristic)
        headerView.text = headerText
        headerView.setTypeface(null, Typeface.BOLD)

        return resultingView
    }

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}