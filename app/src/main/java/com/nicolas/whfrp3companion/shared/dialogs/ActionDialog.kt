package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.nicolas.models.action.Action
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.enums.drawableId
import com.nicolas.whfrp3companion.shared.getView

class ActionDialog : DialogFragment() {
    lateinit var actionTypeImageView: ImageView
    lateinit var actionNameTextView: TextView
    lateinit var actionCooldownTextView: TextView
    lateinit var actionTraitsTextView: TextView
    lateinit var actionSkillsTextView: TextView
    lateinit var actionConditionsTextView: TextView
    lateinit var actionEffectsListView: ListView

    private lateinit var action: Action

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action, null, false)

        action = arguments!!.getSerializable(ACTION_INTENT_ARGUMENT) as Action

        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }

        val dialog = builder.create()
        bindViews(view)
        setupViews()

        return dialog
    }

    private fun setupViews(conservative: Boolean = true) {
        actionTypeImageView.setImageResource(action.type.drawableId)
        actionNameTextView.text = action.name
        actionTraitsTextView.text = action.traits.joinToString()
        actionSkillsTextView.text = action.skillsString
        actionConditionsTextView.text = action.conditionsString

        if (conservative) {
            actionCooldownTextView.text = action.conservativeSide.cooldown.toString()
            action.conservativeSide.effects?.let {
                actionEffectsListView.adapter = ActionEffectsAdapter(context!!, it)
            }
        } else {
            actionCooldownTextView.text = action.recklessSide.cooldown.toString()
            action.recklessSide.effects?.let {
                actionEffectsListView.adapter = ActionEffectsAdapter(context!!, it)
            }
        }

    }

    private fun bindViews(view: View) {
        actionTypeImageView = view.getView(R.id.actionTypeImageView)
        actionNameTextView = view.getView(R.id.actionNameTextView)
        actionCooldownTextView = view.getView(R.id.actionCooldownTextView)
        actionTraitsTextView = view.getView(R.id.actionTraitsTextView)
        actionSkillsTextView = view.getView(R.id.actionSkillsTextView)
        actionConditionsTextView = view.getView(R.id.actionConditionsTextView)
        actionEffectsListView = view.getView(R.id.actionEffectsListView)
    }

    companion object {
        fun newInstance(action: Action): ActionDialog {
            val args = Bundle()
            args.putSerializable(ACTION_INTENT_ARGUMENT, action)

            val fragment = ActionDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
