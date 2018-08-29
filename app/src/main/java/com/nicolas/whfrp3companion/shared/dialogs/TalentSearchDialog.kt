package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.nicolas.models.talent.TalentCooldown
import com.nicolas.models.talent.TalentSearch
import com.nicolas.models.talent.TalentType
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.TalentsActivity
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.getView
import org.jetbrains.anko.intentFor

class TalentSearchDialog : DialogFragment() {
    lateinit var talentTypeSpinner: Spinner
    lateinit var cooldownSpinner: Spinner
    lateinit var searchEditText: EditText

    private var talentSearch: TalentSearch? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_talent_search, null, false)

        talentSearch = arguments!!.getSerializable(TALENTS_SEARCH_INTENT_ARGUMENT) as TalentSearch?
        val playerName = arguments!!.getSerializable(PLAYER_NAME_INTENT_ARGUMENT) as String?

        builder.setView(view)
        builder.setTitle(R.string.search)
        builder.setPositiveButton(R.string.search) { _, _ ->
            launchTalentsActivity(playerName)
        }

        val dialog = builder.create()
        bindViews(view)
        setupViews()

        return dialog
    }

    private fun setupViews() {
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
    }

    private fun bindViews(view: View) {
        talentTypeSpinner = view.getView(R.id.talentTypeSpinner)
        cooldownSpinner = view.getView(R.id.cooldownSpinner)
        searchEditText = view.getView(R.id.searchEditText)
    }

    private fun launchTalentsActivity(playerName: String?) {
        activity?.let {
            startActivity(it.intentFor<TalentsActivity>(
                    TALENTS_SEARCH_INTENT_ARGUMENT to getTalentSearchFromViews(),
                    PLAYER_NAME_INTENT_ARGUMENT to playerName
            ))
            dismiss()

            if (it is TalentsActivity) {
                it.finish()
            }
        }
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
        fun newInstance(): TalentSearchDialog {
            return createFragment(Bundle())
        }

        fun newInstance(talentSearch: TalentSearch): TalentSearchDialog {
            val args = Bundle()
            args.putSerializable(TALENTS_SEARCH_INTENT_ARGUMENT, talentSearch)

            return createFragment(args)
        }

        fun newInstance(playerName: String): TalentSearchDialog {
            val args = Bundle()
            args.putSerializable(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            return createFragment(args)
        }

        private fun createFragment(args: Bundle): TalentSearchDialog =
                TalentSearchDialog().apply { arguments = args }
    }
}
