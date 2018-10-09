package com.nicolas.whfrp3companion.playersheet.talents

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface.BOLD_ITALIC
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.nicolas.models.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.PlayerElementEditionMode
import com.nicolas.whfrp3companion.shared.enums.PlayerElementEditionMode.ADDABLE
import com.nicolas.whfrp3companion.shared.enums.PlayerElementEditionMode.EQUIPABLE_OR_REMOVABLE
import com.nicolas.whfrp3companion.shared.enums.colorId
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.viewModifications.hide
import com.nicolas.whfrp3companion.shared.viewModifications.parseTemplatedText
import com.nicolas.whfrp3companion.shared.viewModifications.show
import org.jetbrains.anko.design.snackbar

class PlayerTalentsAdapter(context: Context,
                           private val talents: List<Talent>,
                           private val talentListener: TalentListener,
                           private val talentEditionMode: PlayerElementEditionMode) : RecyclerView.Adapter<PlayerTalentsAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private val addedTalents = mutableListOf<Talent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_element_player_talent, parent, false)

        return ViewHolder(view, talentListener, talentEditionMode) { talent -> addedTalents.add(talent) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupViews(talents[position], addedTalents)
    }

    override fun getItemCount(): Int = talents.size

    class ViewHolder(private val view: View,
                     private val talentListener: TalentListener,
                     talentEditionMode: PlayerElementEditionMode,
                     private val addTalent: (Talent) -> Unit) : RecyclerView.ViewHolder(view) {
        private val talentTypeTextView by view.bind<TextView>(R.id.talentTypeTextView)
        private val talentNameTextView by view.bind<TextView>(R.id.talentNameTextView)
        private val talentDescriptionTextView by view.bind<TextView>(R.id.talentDescriptionTextView)
        private val addTalentButton by view.bind<ImageButton>(R.id.addTalentButton)
        private val toggleTalentEquipmentButton by view.bind<ImageButton>(R.id.toggleTalentEquipmentButton)
        private val removeTalentButton by view.bind<ImageButton>(R.id.removeTalentButton)

        private lateinit var talent: Talent
        private lateinit var addedTalents: List<Talent>

        init {
            setupViewsEvents()

            when (talentEditionMode) {
                ADDABLE -> {
                    addTalentButton.show()
                    toggleTalentEquipmentButton.hide()
                    removeTalentButton.hide()
                }
                EQUIPABLE_OR_REMOVABLE -> {
                    addTalentButton.hide()
                    toggleTalentEquipmentButton.show()
                    removeTalentButton.show()
                }
            }
        }

        fun setupViews(talent: Talent, addedTalents: List<Talent>) {
            this.talent = talent
            this.addedTalents = addedTalents

            val talentTypeColor = ContextCompat.getColor(view.context, talent.type.colorId)

            talentTypeTextView.setText(talent.type.labelId)
            talentTypeTextView.setBackgroundColor(talentTypeColor)

            talentNameTextView.text = talent.name
            talentNameTextView.setTextColor(talentTypeColor)
            if (talent.isEquipped) {
                talentNameTextView.setTypeface(null, BOLD_ITALIC)
                talentTypeTextView.setTypeface(null, BOLD_ITALIC)
            }

            addTalentButton.imageTintList = ColorStateList.valueOf(talentTypeColor)
            toggleTalentEquipmentButton.imageTintList = ColorStateList.valueOf(talentTypeColor)
            removeTalentButton.imageTintList = ColorStateList.valueOf(talentTypeColor)

            talentDescriptionTextView.text = parseTemplatedText(view.context, talent.description)

            addedTalents.find { it.name == talent.name }?.let {
                this.addTalentButton.hide()
            }
        }

        private fun setupViewsEvents() {
            addTalentButton.setOnClickListener { onAddTalent() }

            toggleTalentEquipmentButton.setOnClickListener { talentListener.onToggleTalentEquipment(talent) }

            removeTalentButton.setOnClickListener { talentListener.onRemoveTalent(talent) }
        }

        private fun onAddTalent() {
            talentListener.onAddTalent(talent)
            addTalent(talent)
            this.addTalentButton.hide()


            val snackbarText = view.context.getString(R.string.talent_added_format).format(talent.name)
            view.snackbar(snackbarText).show()
        }
    }
}