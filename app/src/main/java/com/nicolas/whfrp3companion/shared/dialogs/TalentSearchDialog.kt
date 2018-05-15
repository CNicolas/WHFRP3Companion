package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.models.player.playerLinked.talent.TalentCooldown
import com.nicolas.models.player.playerLinked.talent.TalentType
import com.nicolas.playersheet.dtos.TalentSearch
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.TalentsActivity
import com.nicolas.whfrp3companion.shared.enums.labelId
import kotlinx.android.synthetic.main.dialog_talent_search.*
import org.jetbrains.anko.intentFor

class TalentSearchDialog : DialogFragment() {
    private lateinit var unbinder: Unbinder

    private var talentSearch: TalentSearch? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_talent_search, null, false)

        talentSearch = arguments!!.getSerializable(TALENTS_SEARCH_INTENT_ARGUMENT) as TalentSearch?

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

        val talentTypePosition = talentSearch?.talentType?.ordinal
        if (talentTypePosition != null) {
            talentTypeSpinner.setSelection(talentTypePosition + 1)
        }
        val talentCooldownPosition = talentSearch?.cooldown?.ordinal
        if (talentCooldownPosition != null) {
            cooldownSpinner.setSelection(talentCooldownPosition + 1)
        }
        searchEditText.setText(talentSearch?.text)

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

    companion object {
        fun newInstance(talentSearch: TalentSearch? = null): TalentSearchDialog {
            val args = Bundle()
            if (talentSearch != null) {
                args.putSerializable(TALENTS_SEARCH_INTENT_ARGUMENT, talentSearch)
            }

            val fragment = TalentSearchDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
