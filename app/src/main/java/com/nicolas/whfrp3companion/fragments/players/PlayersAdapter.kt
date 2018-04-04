package com.nicolas.whfrp3companion.fragments.players

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.entities.player.Player

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
            view = inflater.inflate(R.layout.list_element_player, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        val player = getItem(position)

        holder.name.text = player.name
        holder.race.text = view.context.getString(player.race.labelId)

        return view
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.player_name)
        lateinit var name: TextView
        @BindView(R.id.race)
        lateinit var race: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}