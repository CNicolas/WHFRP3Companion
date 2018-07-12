package com.nicolas.whfrp3companion.playersheet.skills

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.BOLD_ITALIC
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.nicolas.database.anko.AnkoPlayerRepository
import com.nicolas.models.extensions.createHand
import com.nicolas.models.extensions.getSkillByName
import com.nicolas.models.extensions.getSpecializationByName
import com.nicolas.models.player.Player
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.SkillType.ADVANCED
import com.nicolas.models.skill.SkillType.BASIC
import com.nicolas.models.skill.Specialization
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.DiceRollerActivity
import com.nicolas.whfrp3companion.shared.adapters.AbstractSkillsExpandableAdapter
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.getView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor

class PlayerSkillsExpandableAdapter(context: Context,
                                    private val player: Player) : AbstractSkillsExpandableAdapter(player.skills) {
    private val inflater = LayoutInflater.from(context)

    private val ankoPlayerRepository: AnkoPlayerRepository = AnkoPlayerRepository(context)

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getGroupViewHolderOfView(convertView, parent)

        holder.player = player
        holder.ankoPlayerRepository = ankoPlayerRepository

        holder.setupViews(getGroup(groupPosition))

        return resultingView
    }

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getChildViewHolderOfView(convertView, parent)

        holder.player = player
        holder.ankoPlayerRepository = ankoPlayerRepository

        holder.setupViews(getGroup(groupPosition), getChild(groupPosition, childPosition))

        return resultingView
    }

    private fun getChildViewHolderOfView(savedView: View?, parent: ViewGroup?): Pair<View, ChildViewHolder> {
        var view = savedView

        val holder: ChildViewHolder
        if (view != null) {
            holder = view.tag as ChildViewHolder
        } else {
            view = inflater.inflate(R.layout.list_player_skills_child, parent, false)
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
            view = inflater.inflate(R.layout.list_player_skills_header, parent, false)
            holder = GroupViewHolder(view)
            view!!.tag = holder
        }

        return Pair(view, holder)
    }

    internal class ChildViewHolder(view: View) {
        private val specializationNameCheckableView by view.bind<CheckBox>(R.id.specialization_name_checkable)
        private val launchSpecializationImageButton by view.bind<ImageButton>(R.id.launch_specialization)

        internal var player: Player? = null
        internal var ankoPlayerRepository: AnkoPlayerRepository? = null

        lateinit var skill: Skill
        lateinit var specialization: Specialization

        init {
            setupViewsEvents(view)
        }

        fun setupViews(skill: Skill, specialization: Specialization) {
            this.skill = skill
            this.specialization = specialization

            specializationNameCheckableView.text = specialization.name
            specializationNameCheckableView.isChecked = specialization.isSpecialized

            specializationNameCheckableView.isFocusable = false
            launchSpecializationImageButton.isFocusable = false
        }

        private fun setupViewsEvents(view: View) {
            view.getView<CheckBox>(R.id.specialization_name_checkable)
                    .setOnClickListener { toggleSpecialization(it as CheckBox) }

            launchSpecializationImageButton
                    .setOnClickListener { startDiceRollerActivityForSpecialization(it) }
        }

        private fun toggleSpecialization(checkbox: CheckBox) {
            specialization.isSpecialized = !specialization.isSpecialized
            checkbox.isChecked = specialization.isSpecialized

            doAsync {
                if (player != null) {
                    player?.getSkillByName(skill.name)
                            ?.getSpecializationByName(specialization.name)
                            ?.isSpecialized = specialization.isSpecialized
                    ankoPlayerRepository?.update(player!!)
                }
            }
        }

        private fun startDiceRollerActivityForSpecialization(view: View) {
            val context = view.context
            context.startActivity(context.intentFor<DiceRollerActivity>(
                    HAND_INTENT_ARGUMENT to player?.createHand(skill, specialization, skill.name)
            ))
        }

    }

    internal class GroupViewHolder(private val view: View) {
        private val level1CheckBox by view.bind<CheckBox>(R.id.level_1)
        private val level2CheckBox by view.bind<CheckBox>(R.id.level_2)
        private val level3CheckBox by view.bind<CheckBox>(R.id.level_3)
        private val skillNameTextView by view.bind<TextView>(R.id.skill_name)
        private val characteristicTextView by view.bind<TextView>(R.id.characteristic)
        private val launchSkillImageButton by view.bind<ImageButton>(R.id.launch_skill)

        internal var player: Player? = null
        internal var ankoPlayerRepository: AnkoPlayerRepository? = null

        lateinit var skill: Skill

        init {
            setupViewsEvents()
        }

        fun setupViews(skill: Skill) {
            this.skill = skill

            skillNameTextView.text = skill.name
            when (skill.type) {
                BASIC -> skillNameTextView.setTypeface(null, BOLD)
                ADVANCED -> skillNameTextView.setTypeface(null, BOLD_ITALIC)
            }

            characteristicTextView.text = view.context.getString(skill.characteristic.labelId).substring(0..2)

            checkLevel()
            loseFocus()
        }

        private fun setupViewsEvents() {
            level1CheckBox.setOnClickListener { setFormationLevel(1) }
            level2CheckBox.setOnClickListener { setFormationLevel(2) }
            level3CheckBox.setOnClickListener { setFormationLevel(3) }

            launchSkillImageButton.setOnClickListener { startDiceRollerActivityForSkill() }
        }

        private fun loseFocus() {
            launchSkillImageButton.isFocusable = false
            level1CheckBox.isFocusable = false
            level2CheckBox.isFocusable = false
            level3CheckBox.isFocusable = false
        }

        private fun setFormationLevel(level: Int) {
            when (level) {
                skill.level -> skill.level = 0
                else -> skill.level = level
            }

            checkLevel()
            loseFocus()

            doAsync {
                if (player != null) {
                    player?.getSkillByName(skill.name)?.level = skill.level
                    ankoPlayerRepository?.update(player!!)
                }
            }
        }

        private fun checkLevel() {
            when (skill.level) {
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

        private fun startDiceRollerActivityForSkill() {
            val context = view.context
            context.startActivity(context.intentFor<DiceRollerActivity>(
                    HAND_INTENT_ARGUMENT to player?.createHand(skill, skill.name)
            ))
        }
    }
}