package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.colorId
import org.jetbrains.anko.toast

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
        private val nameView by view.bind<TextView>(R.id.talent_name)
        private lateinit var talent: Talent

        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(talent: Talent) {
            this.talent = talent

            nameView.text = talent.name

            val talentTypeColor = ContextCompat.getColor(nameView.context, talent.type.colorId)
            nameView.setTextColor(talentTypeColor)
        }

        @OnClick(R.id.talent_name)
        fun selectTalent() {
            view.context.toast(talent.toString())
        }
    }
}