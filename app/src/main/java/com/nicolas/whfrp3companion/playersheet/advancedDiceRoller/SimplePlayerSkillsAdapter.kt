package com.nicolas.whfrp3companion.playersheet.advancedDiceRoller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.nicolas.models.skill.Skill
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.shortLabelId

class SimplePlayerSkillsAdapter(context: Context,
                                private val skills: List<Skill>,
                                private val selectSkill: (skill: Skill) -> Unit) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = skills.size
    override fun getItem(position: Int) = skills[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.list_player_skills_header, parent, false)
            holder = ViewHolder(view, selectSkill)
            view!!.tag = holder
        }

        holder.setupViews(getItem(position))

        return view
    }

    internal class ViewHolder(private val view: View,
                              private val selectSkill: (skill: Skill) -> Unit) {

        private val level1CheckBox by view.bind<CheckBox>(R.id.level_1)
        private val level2CheckBox by view.bind<CheckBox>(R.id.level_2)
        private val level3CheckBox by view.bind<CheckBox>(R.id.level_3)
        private val skillNameTextView by view.bind<TextView>(R.id.skill_name)
        private val characteristicTextView by view.bind<TextView>(R.id.characteristic)
        private val launchSkillImageButton by view.bind<ImageButton>(R.id.launch_skill)

        lateinit var skill: Skill

        init {
            level1CheckBox.isEnabled = false
            level2CheckBox.isEnabled = false
            level3CheckBox.isEnabled = false
            launchSkillImageButton.visibility = View.GONE

            view.setOnClickListener { selectSkill(skill) }
        }

        fun setupViews(skill: Skill) {
            this.skill = skill

            skillNameTextView.text = skill.name
            characteristicTextView.text = view.context.getString(skill.characteristic.shortLabelId)

            checkLevel(skill.level)
        }

        private fun checkLevel(level: Int) = when (level) {
            1 -> {
                level1CheckBox.isChecked = true
                level2CheckBox.isChecked = false
                level3CheckBox.isChecked = false
            }
            2 -> {
                level1CheckBox.isChecked = true
                level2CheckBox.isChecked = true
                level3CheckBox.isChecked = false
            }
            3 -> {
                level1CheckBox.isChecked = true
                level2CheckBox.isChecked = true
                level3CheckBox.isChecked = true
            }
            else -> {
                level1CheckBox.isChecked = false
                level2CheckBox.isChecked = false
                level3CheckBox.isChecked = false
            }
        }
    }
}