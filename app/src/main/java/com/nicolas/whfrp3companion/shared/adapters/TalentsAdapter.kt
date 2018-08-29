package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nicolas.models.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.colorId
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.viewModifications.parseTemplatedText

class TalentsAdapter(context: Context, private val talents: List<Talent>) : RecyclerView.Adapter<TalentsAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_element_talent, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupViews(talents[position])
    }

    override fun getItemCount(): Int = talents.size

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val typeView by view.bind<TextView>(R.id.talent_type)
        private val nameView by view.bind<TextView>(R.id.talent_name)
        private val descriptionView by view.bind<TextView>(R.id.talent_description)

        private lateinit var talent: Talent

        fun setupViews(talent: Talent) {
            this.talent = talent

            val talentTypeColor = ContextCompat.getColor(view.context, talent.type.colorId)

            typeView.setText(talent.type.labelId)
            typeView.setBackgroundColor(talentTypeColor)

            nameView.text = talent.name
            nameView.setTextColor(talentTypeColor)

            descriptionView.text = parseTemplatedText(view.context, talent.description)
        }
    }
}