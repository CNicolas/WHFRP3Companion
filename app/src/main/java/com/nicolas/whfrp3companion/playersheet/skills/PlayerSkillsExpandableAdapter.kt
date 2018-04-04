package com.nicolas.whfrp3companion.playersheet.skills

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.playersheet.extensions.createHand
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.DiceRollerActivity
import com.nicolas.whfrp3companion.shared.adapters.AbstractSkillsExpandableAdapter
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType.ADVANCED
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType.BASIC
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Specialization
import com.nicolas.whfrp3database.extensions.getSkillByName
import com.nicolas.whfrp3database.extensions.getSpecializationByName
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor

class PlayerSkillsExpandableAdapter(private val context: Context,
                                    private val player: Player) : AbstractSkillsExpandableAdapter(player.skills) {
    private val inflater = LayoutInflater.from(context)

    private val playerFacade: PlayerFacade = PlayerFacade(context)

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getGroupViewHolderOfView(convertView, parent)

        val skill = getGroup(groupPosition)

        holder.player = player
        holder.playerFacade = playerFacade
        holder.skill = skill

        holder.skillNameView.text = skill.name
        when (skill.type) {
            BASIC -> holder.skillNameView.setTypeface(null, Typeface.BOLD)
            ADVANCED -> holder.skillNameView.setTypeface(null, Typeface.BOLD_ITALIC)
        }
        holder.characteristicView.text = context.getString(skill.characteristic.labelId).substring(0..2)

        when (skill.level) {
            1 -> holder.formationLevelsView.check(holder.level1View.id)
            2 -> holder.formationLevelsView.check(holder.level2View.id)
            3 -> holder.formationLevelsView.check(holder.level3View.id)
            else -> holder.formationLevelsView.clearCheck()
        }


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
        holder.playerFacade = playerFacade
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
        @BindView(R.id.specialization_name_checkable)
        lateinit var specializationNameCheckableView: CheckBox
        @BindView(R.id.launch_specialization)
        lateinit var launchSpecializationView: ImageButton

        internal var player: Player? = null
        internal var playerFacade: PlayerFacade? = null

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
                    playerFacade?.update(player!!)
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
        @BindView(R.id.formation_levels)
        lateinit var formationLevelsView: RadioGroup
        @BindView(R.id.level_1)
        lateinit var level1View: RadioButton
        @BindView(R.id.level_2)
        lateinit var level2View: RadioButton
        @BindView(R.id.level_3)
        lateinit var level3View: RadioButton

        @BindView(R.id.skill_name)
        lateinit var skillNameView: TextView
        @BindView(R.id.characteristic)
        lateinit var characteristicView: TextView

        @BindView(R.id.launch_skill)
        lateinit var launchSkillView: ImageButton

        internal var player: Player? = null
        internal var playerFacade: PlayerFacade? = null

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
            formationLevelsView.isFocusable = false
            level1View.isFocusable = false
            level2View.isFocusable = false
            level3View.isFocusable = false
        }

        private fun setFormationLevel(level: Int) {
            when (level) {
                skill.level -> {
                    skill.level = 0
                    formationLevelsView.clearCheck()
                }
                else -> skill.level = level
            }

            loseFocus()

            doAsync {
                if (player != null) {
                    player?.getSkillByName(skill.name)?.level = level
                    playerFacade?.update(player!!)
                }
            }
        }
    }
}