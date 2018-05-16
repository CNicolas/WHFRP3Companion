package com.nicolas.whfrp3companion.playersheet.talents

import android.content.Context
import android.graphics.Typeface.BOLD_ITALIC
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.talents.TalentEditionMode.*
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.colorId
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.viewModifications.parseTemplatedText

class PlayerTalentsAdapter(context: Context,
                           private val talents: List<Talent>,
                           private val talentEditionMode: TalentEditionMode) : RecyclerView.Adapter<PlayerTalentsAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    private val talentListeners: MutableList<TalentListener> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_element_player_talent, parent, false)

        return ViewHolder(view, talentListeners, talentEditionMode)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupViews(talents[position])
    }

    override fun getItemCount(): Int = talents.size

    fun addTalentListener(talentListener: TalentListener) = talentListeners.add(talentListener)

    class ViewHolder(private val view: View, private val talentListeners: List<TalentListener>, talentEditionMode: TalentEditionMode) : RecyclerView.ViewHolder(view) {
        private val talentTypeTextView by view.bind<TextView>(R.id.talentTypeTextView)
        private val talentNameTextView by view.bind<TextView>(R.id.talentNameTextView)
        private val talentDescriptionTextView by view.bind<TextView>(R.id.talentDescriptionTextView)
        private val addTalentButton by view.bind<ImageButton>(R.id.addTalentButton)
        private val removeTalentButton by view.bind<ImageButton>(R.id.removeTalentButton)

        private lateinit var talent: Talent

        init {
            ButterKnife.bind(this, view)

            when (talentEditionMode) {
                ADDABLE -> {
                    addTalentButton.visibility = View.VISIBLE
                    removeTalentButton.visibility = View.GONE
                }
                EQUIPABLE_OR_REMOVABLE -> {
                    addTalentButton.visibility = View.GONE
                    removeTalentButton.visibility = View.VISIBLE
                }
                REMOVABLE -> {
                    addTalentButton.visibility = View.GONE
                    removeTalentButton.visibility = View.VISIBLE
                }
            }
        }

        fun setupViews(talent: Talent) {
            this.talent = talent

            val talentTypeColor = ContextCompat.getColor(view.context, talent.type.colorId)

            talentTypeTextView.setText(talent.type.labelId)
            talentTypeTextView.setBackgroundColor(talentTypeColor)

            talentNameTextView.text = talent.name
            talentNameTextView.setTextColor(talentTypeColor)
            if (talent.isEquipped) {
                talentNameTextView.setTypeface(null, BOLD_ITALIC)
            }

            talentDescriptionTextView.text = parseTemplatedText(view.context, talent.description)
        }

        @OnClick(R.id.addTalentButton)
        fun onAddTalent() {
            talentListeners.notifyAddTalent(talent)
        }

        @OnClick(R.id.toggleTalentEquipmentButton)
        fun onToggleTalentEquipment() {
            talentListeners.notifyToggleTalentEquipment(talent)
        }

        @OnClick(R.id.removeTalentButton)
        fun onRemoveTalent() {
            talentListeners.notifyRemoveTalent(talent)
        }
    }
}