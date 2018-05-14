package com.nicolas.whfrp3companion.fragments.talents

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.ButterKnife
import com.nicolas.models.player.playerLinked.talent.TalentType
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.colorId
import com.nicolas.whfrp3companion.shared.enums.labelId

class TalentTypesAdapter(context: Context) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = TalentType.values().size
    override fun getItem(position: Int) = TalentType[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.list_element_talent_type, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        holder.setupViews(getItem(position))

        return view
    }

    internal class ViewHolder(view: View) {
        private val talentTypeTextView by view.bind<TextView>(R.id.talent_type)

        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(talentType: TalentType) {
            talentTypeTextView.setText(talentType.labelId)

            val talentTypeColor = ContextCompat.getColor(talentTypeTextView.context, talentType.colorId)
            talentTypeTextView.setTextColor(talentTypeColor)
        }
    }
}