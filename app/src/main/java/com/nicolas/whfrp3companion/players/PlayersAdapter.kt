package com.nicolas.whfrp3companion.players

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import warhammer.database.entities.player.Player

class PlayersAdapter(context: Context, private val players: List<Player>) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = players.size
    override fun getItem(position: Int) = players[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        val player = getItem(position)

        holder.name.text = player.name

        return view
    }

    internal class ViewHolder(view: View) {
        @BindView(android.R.id.text1)
        lateinit var name: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}