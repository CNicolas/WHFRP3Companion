package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.ButterKnife
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind

class TalentsAdapter(context: Context, private val talents: List<Talent>) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = talents.size
    override fun getItem(position: Int) = talents[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.list_element_talent, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        val talent = getItem(position)

        holder.nameView.text = talent.name

        return view
    }

    internal class ViewHolder(view: View) {
        val nameView by view.bind<TextView>(R.id.talent_name)

        init {
            ButterKnife.bind(this, view)
        }
    }
}