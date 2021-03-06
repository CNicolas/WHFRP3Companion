package com.nicolas.whfrp3companion.playersheet.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.nicolas.models.item.Weapon
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.drawableId
import com.nicolas.whfrp3companion.shared.enums.labelId

class WeaponsAdapter(context: Context, private val weapons: List<Weapon>) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = weapons.size
    override fun getItem(position: Int) = weapons[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.list_element_weapon, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        holder.setupViews(getItem(position))

        return view
    }

    internal class ViewHolder(view: View) {
        private val weaponTypeImageView by view.bind<ImageView>(R.id.weapon_type)
        private val weaponNameTextView by view.bind<TextView>(R.id.weapon_name)

        private val damageTextView by view.bind<TextView>(R.id.damage)
        private val criticalLevelTextView by view.bind<TextView>(R.id.critical_level)
        private val rangeTextView by view.bind<TextView>(R.id.range)

        private lateinit var weapon: Weapon

        fun setupViews(weapon: Weapon) {
            this.weapon = weapon

            weaponTypeImageView.setImageResource(weapon.subType.drawableId)

            weaponNameTextView.text = weapon.name

            damageTextView.text = weapon.damage.toString()
            criticalLevelTextView.text = weapon.criticalLevel.toString()
            rangeTextView.setText(weapon.range.labelId)
        }
    }
}