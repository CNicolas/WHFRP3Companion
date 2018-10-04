package com.nicolas.whfrp3companion.shared.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.nicolas.models.action.ActionSearch
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.Trait
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.enums.sortedAndLabels
import kotlinx.android.synthetic.main.activity_search_action.*
import kotlinx.android.synthetic.main.toolbar.*

class ActionSearchActivity : AppCompatActivity() {
    private var traits: List<Trait> = listOf()
    private lateinit var actionTypesWithLabels: Pair<List<ActionType>, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_action)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViews()
        setupViewsEvents()
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()

        return true
    }

    private fun setupViews() {
        actionTypesWithLabels = ActionType.values().sortedAndLabels(this)

        action_type.adapter = ArrayAdapter(this,
                R.layout.element_enum_spinner,
                listOf(getString(R.string.all)) + actionTypesWithLabels.second)
    }

    private fun setupViewsEvents() {
        search.setOnClickListener { closeActivityWithActionSearch() }
        traits_textview.setOnClickListener { openTraitsSelector() }
    }

    private fun getActionSearchFromViews(): ActionSearch {
        val text = search_text.text.toString().trim()

        val actionType = when {
            action_type.selectedItemPosition == 0 -> null
            else -> actionTypesWithLabels.first[action_type.selectedItemPosition - 1]
        }

        val cooldown = cooldown.text.toString().toIntOrNull()

        return ActionSearch(text = text,
                actionType = actionType,
                traits = traits,
                cooldown = cooldown)
    }

    private fun closeActivityWithActionSearch() {
        val resultingIntent = Intent()
        resultingIntent.putExtra(ACTION_SEARCH_INTENT_ARGUMENT, getActionSearchFromViews())

        setResult(Activity.RESULT_OK, resultingIntent)
        finish()
    }

    private fun openTraitsSelector() {
        val sortedTraitsWithLabels = Trait.values().sortedAndLabels(this)
        val selectedTraits = mutableListOf<Trait>()
        val initialSelection = BooleanArray(Trait.values().size) { false }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.action_traits)
                .setMultiChoiceItems(sortedTraitsWithLabels.second.toTypedArray(), initialSelection) { _, pos, isChecked ->
                    when {
                        isChecked -> selectedTraits.add(sortedTraitsWithLabels.first[pos])
                        else -> selectedTraits.remove(sortedTraitsWithLabels.first[pos])
                    }
                }
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    traits = selectedTraits
                    dialog.dismiss()

                    traits_textview.text = traits.joinToString(", ") { getString(it.labelId) }
                }
    }
}