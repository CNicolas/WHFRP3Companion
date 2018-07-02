package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.ButterKnife
import com.nicolas.models.action.effect.*
import com.nicolas.whfrp3companion.shared.bind

class ActionEffectsAdapter(context: Context, private val effects: ActionEffects) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = effects.size
    override fun getItem(position: Int) = effects[position]
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

        val effect = getItem(position)!!
        holder.setupViews(effect)

        return view
    }

    internal class ViewHolder(view: View) {
        private lateinit var effect: ActionEffect

        private val textTextView by view.bind<TextView>(android.R.id.text1)

        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(effect: ActionEffect) {
            this.effect = effect

            textTextView.text = "${this.effect.face} damage:${this.effect.effect.damage}"
        }
    }
}