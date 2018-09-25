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
import com.nicolas.models.action.ActionSearch
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.Trait
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.getView

class ActionSearchDialog : DialogFragment() {
    private lateinit var actionTypeSpinner: Spinner
    private lateinit var actionTraitSpinner: Spinner
    private lateinit var cooldownEditText: EditText
    private lateinit var searchEditText: EditText

    private lateinit var handleSearch: (actionSearch: ActionSearch) -> Unit

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action_search, null, false)

        builder.setView(view)
        builder.setTitle(R.string.search)
        builder.setPositiveButton(R.string.search) { _, _ ->
            handleSearch(getTalentSearchFromViews())
            dismiss()
        }

        val dialog = builder.create()
        bindViews(view)
        setupViews()

        return dialog
    }

    private fun setupViews() {
        val sortedActionTypesLabels = ActionType.values()
                .map { getString(it.labelId) }
                .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it })
        val actionTypes = listOf(getString(R.string.all)) + sortedActionTypesLabels

        val traits = listOf(getString(R.string.all)) + Trait.values().map { getString(it.labelId) }

        actionTypeSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, actionTypes)
        actionTraitSpinner.adapter = ArrayAdapter(context, R.layout.element_enum_spinner, traits)
    }

    private fun bindViews(view: View) {
        actionTypeSpinner = view.getView(R.id.actionTypeSpinner)
        actionTraitSpinner = view.getView(R.id.actionTraitSpinner)
        cooldownEditText = view.getView(R.id.cooldownEditText)
        searchEditText = view.getView(R.id.searchEditText)
    }

    private fun getTalentSearchFromViews(): ActionSearch {
        val text = searchEditText.text.toString().trim()
        val actionType = when {
            actionTypeSpinner.selectedItemPosition == 0 -> null
            else -> ActionType[actionTypeSpinner.selectedItemPosition - 1]
        }
        val trait = when {
            actionTraitSpinner.selectedItemPosition == 0 -> null
            else -> Trait[actionTraitSpinner.selectedItemPosition - 1]
        }
        val cooldown = cooldownEditText.text.toString().toIntOrNull()

        return ActionSearch(text = text,
                actionType = actionType,
                trait = trait,
                cooldown = cooldown)
    }

    companion object {
        fun newInstance(searchAction: (actionSearch: ActionSearch) -> Unit): ActionSearchDialog =
                ActionSearchDialog().apply {
                    handleSearch = searchAction
                }
    }
}
