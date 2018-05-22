package com.nicolas.whfrp3companion.playersheet.skills

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.database.anko.AnkoPlayerRepository
import com.nicolas.models.extensions.getSkillByName
import com.nicolas.models.extensions.getSpecializationByName
import com.nicolas.models.player.Player
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.SkillType.ADVANCED
import com.nicolas.models.player.skill.SkillType.BASIC
import com.nicolas.models.player.skill.Specialization
import com.nicolas.playersheet.extensions.createHand
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.DiceRollerActivity
import com.nicolas.whfrp3companion.shared.adapters.AbstractSkillsExpandableAdapter
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.labelId
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor

class PlayerSkillsExpandableAdapter(private val context: Context,
                                    private val player: Player) : AbstractSkillsExpandableAdapter(player.skills) {
    private val inflater = LayoutInflater.from(context)

    private val ankoPlayerRepository: AnkoPlayerRepository = AnkoPlayerRepository(context)

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getGroupViewHolderOfView(convertView, parent)

        val skill = getGroup(groupPosition)

        holder.player = player
        holder.ankoPlayerRepository = ankoPlayerRepository
        holder.skill = skill

        holder.skillNameView.text = skill.name
        when (skill.type) {
            BASIC -> holder.skillNameView.setTypeface(null, Typeface.BOLD)
            ADVANCED -> holder.skillNameView.setTypeface(null, Typeface.BOLD_ITALIC)
        }
        holder.characteristicView.text = context.getString(skill.characteristic.labelId).substring(0..2)

        holder.checkLevel()
        holder.loseFocus()

        return resultingView
    }

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getChildViewHolderOfView(convertView, parent)

        val specialization = getChild(groupPosition, childPosition)

        holder.player = player
        holder.ankoPlayerRepository = ankoPlayerRepository
        holder.skill = getGroup(groupPosition)
        holder.specialization = specialization

        holder.specializationNameCheckableView.text = specialization.name
        holder.specializationNameCheckableView.isChecked = specialization.isSpecialized

        holder.loseFocus()

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

    internal class ChildViewHolder(private val view: View) {
        val specializationNameCheckableView by view.bind<CheckBox>(R.id.specialization_name_checkable)
        private val launchSpecializationView by view.bind<ImageButton>(R.id.launch_specialization)

        internal var player: Player? = null
        internal var ankoPlayerRepository: AnkoPlayerRepository? = null

        lateinit var skill: Skill
        lateinit var specialization: Specialization

        init {
            ButterKnife.bind(this, view)
        }

        @OnClick(R.id.specialization_name_checkable)
        fun toggleSpecialization(view: View) {
            specialization.isSpecialized = !specialization.isSpecialized
            (view as CheckBox).isChecked = specialization.isSpecialized

            doAsync {
                if (player != null) {
                    player?.getSkillByName(skill.name)
                            ?.getSpecializationByName(specialization.name)
                            ?.isSpecialized = specialization.isSpecialized
                    ankoPlayerRepository?.update(player!!)
                }
            }
        }

        @OnClick(R.id.launch_specialization)
        fun launchSpecialization() {
            val context = view.context
            context.startActivity(context.intentFor<DiceRollerActivity>(
                    HAND_INTENT_ARGUMENT to player?.createHand(skill, specialization, skill.name)
            ))
        }

        fun loseFocus() {
            specializationNameCheckableView.isFocusable = false
            launchSpecializationView.isFocusable = false
        }
    }

    internal class GroupViewHolder(private val view: View) {
        private val level1View by view.bind<CheckBox>(R.id.level_1)
        private val level2View by view.bind<CheckBox>(R.id.level_2)
        private val level3View by view.bind<CheckBox>(R.id.level_3)

        val skillNameView by view.bind<TextView>(R.id.skill_name)
        val characteristicView by view.bind<TextView>(R.id.characteristic)

        private val launchSkillView by view.bind<ImageButton>(R.id.launch_skill)

        internal var player: Player? = null
        internal var ankoPlayerRepository: AnkoPlayerRepository? = null

        lateinit var skill: Skill

        init {
            ButterKnife.bind(this, view)
        }

        @OnClick(R.id.level_1)
        fun checkFormationLevel1() = setFormationLevel(1)

        @OnClick(R.id.level_2)
        fun checkFormationLevel2() = setFormationLevel(2)

        @OnClick(R.id.level_3)
        fun checkFormationLevel3() = setFormationLevel(3)

        @OnClick(R.id.launch_skill)
        fun launchSkill() {
            val context = view.context
            context.startActivity(context.intentFor<DiceRollerActivity>(
                    HAND_INTENT_ARGUMENT to player?.createHand(skill, skill.name)
            ))
        }

        fun loseFocus() {
            launchSkillView.isFocusable = false
            level1View.isFocusable = false
            level2View.isFocusable = false
            level3View.isFocusable = false
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

        fun checkLevel() {
            when (skill.level) {
                1 -> {
                    level1View.isChecked = true
                    level2View.isChecked = false
                    level3View.isChecked = false
                }
                2 -> {
                    level1View.isChecked = true
                    level2View.isChecked = true
                    level3View.isChecked = false
                }
                3 -> {
                    level1View.isChecked = true
                    level2View.isChecked = true
                    level3View.isChecked = true
                }
                else -> {
                    level1View.isChecked = false
                    level2View.isChecked = false
                    level3View.isChecked = false
                }
            }
        }
    }
}