package com.nicolas.whfrp3companion.playersheet.advancedDiceRoller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.ListView
import com.nicolas.models.item.Weapon
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.state.WeaponsAdapter
import com.nicolas.whfrp3companion.shared.WEAPONS_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.getView

class ActionWeaponDialog() : DialogFragment() {
    private var chooseWeapon: (weapon: Weapon) -> Unit = {}

    constructor(chooseWeapon: (weapon: Weapon) -> Unit) : this() {
        this.chooseWeapon = chooseWeapon
    }

    private lateinit var weapons: List<Weapon>

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action_weapons, null, false)

        weapons = (arguments!!.getSerializable(WEAPONS_INTENT_ARGUMENT) as Array<Weapon>).toList()

        builder.setView(view)
        builder.setTitle(R.string.weapon)

        val dialog = builder.create()
        setupViews(view)

        return dialog
    }

    private fun setupViews(view: View) {
        val weaponsListView = view.getView<ListView>(R.id.weapons_listview)
        weaponsListView.adapter = WeaponsAdapter(view.context, weapons)
        weaponsListView.setOnItemClickListener { _, _, position, _ ->
            chooseWeapon(weapons[position])
            dismiss()
        }
    }

    companion object {
        fun newInstance(weapons: List<Weapon>, chooseWeapon: (weapon: Weapon) -> Unit): ActionWeaponDialog {
            val args = Bundle()
            args.putSerializable(WEAPONS_INTENT_ARGUMENT, weapons.toTypedArray())

            val fragment = ActionWeaponDialog(chooseWeapon)
            fragment.arguments = args

            return fragment
        }
    }
}