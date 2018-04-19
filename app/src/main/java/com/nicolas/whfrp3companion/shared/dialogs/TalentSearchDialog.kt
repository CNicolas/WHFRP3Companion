package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType
import com.nicolas.whfrp3database.staticData.loadSkills

class TalentSearchDialog : DialogFragment() {
    @BindView(R.id.talent_type)
    lateinit var talentTypeSpinner: Spinner
    @BindView(R.id.cooldown)
    lateinit var cooldownSpinner: Spinner
    @BindView(R.id.talent_name)
    lateinit var talentNameEditText: EditText
    @BindView(R.id.description)
    lateinit var descriptionEditText: EditText
    @BindView(R.id.characteristic)
    lateinit var characteristicSpinner: Spinner
    @BindView(R.id.skill)
    lateinit var skillsSpinner: Spinner

    private lateinit var unbinder: Unbinder

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_talent_search, null, false)

        builder.setView(view)
        builder.setTitle(R.string.search)
        builder.setPositiveButton(R.string.search, { _, _ -> dismiss() })

        val dialog = builder.create()
        unbinder = ButterKnife.bind(this, view)

        val talentTypes = listOf(getString(R.string.all)) + TalentType.values().map { getString(it.labelId) }
        val talentCooldowns = listOf(getString(R.string.all)) + TalentCooldown.values().map { getString(it.labelId) }
        val characteristics = listOf(getString(R.string.all)) + Characteristic.values().map { getString(it.labelId) }
        val skills = listOf(getString(R.string.all)) + loadSkills(context!!).map { it.name }

        talentTypeSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, talentTypes)
        cooldownSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, talentCooldowns)
        characteristicSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, characteristics)
        skillsSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, skills)

        return dialog
    }
}
