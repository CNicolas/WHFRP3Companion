package com.nicolas.whfrp3companion.fragments.players

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.nicolas.whfrp3companion.components.labelId
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
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        val player = getItem(position)

        holder.playerText.text = "${player.name} - ${view.context.getString(player.race.labelId)}"

        return view
    }

    internal class ViewHolder(view: View) {
        @BindView(android.R.id.text1)
        lateinit var playerText: TextView

        init {
            ButterKnife.bind(this, view)
            playerText.gravity = Gravity.CENTER
            playerText.textAlignment = TEXT_ALIGNMENT_CENTER
        }
    }
}