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
import com.nicolas.playersheet.dtos.TalentSearch
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.TalentsActivity
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType
import org.jetbrains.anko.intentFor

class TalentSearchDialog : DialogFragment() {
    @BindView(R.id.talent_type)
    lateinit var talentTypeSpinner: Spinner
    @BindView(R.id.cooldown)
    lateinit var cooldownSpinner: Spinner
    @BindView(R.id.search)
    lateinit var searchEditText: EditText

    private lateinit var unbinder: Unbinder

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_talent_search, null, false)

        builder.setView(view)
        builder.setTitle(R.string.search)
        builder.setPositiveButton(R.string.search, { _, _ ->
            if (activity != null) {
                startActivity(activity!!.intentFor<TalentsActivity>(
                        TALENTS_SEARCH_INTENT_ARGUMENT to getTalentSearchFromViews()
                ))
                dismiss()
            }
        })

        val dialog = builder.create()
        unbinder = ButterKnife.bind(this, view)

        val talentTypes = listOf(getString(R.string.all)) + TalentType.values().map { getString(it.labelId) }
        val talentCooldowns = listOf(getString(R.string.all)) + TalentCooldown.values().map { getString(it.labelId) }

        talentTypeSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, talentTypes)
        cooldownSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, talentCooldowns)

        return dialog
    }

    private fun getTalentSearchFromViews(): TalentSearch {
        val talentType = when {
            talentTypeSpinner.selectedItemPosition == 0 -> null
            else -> TalentType[talentTypeSpinner.selectedItemPosition - 1]
        }

        val talentCooldown = when {
            cooldownSpinner.selectedItemPosition == 0 -> null
            else -> TalentCooldown[cooldownSpinner.selectedItemPosition - 1]
        }

        return TalentSearch(
                text = searchEditText.text.toString(),
                talentType = talentType,
                cooldown = talentCooldown)
    }
}
