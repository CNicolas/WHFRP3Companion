package com.nicolas.whfrp3companion.playersheet.actions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.nicolas.diceroller.roll.RollResult
import com.nicolas.diceroller.roll.getRollResult
import com.nicolas.models.action.Action
import com.nicolas.models.extensions.getSideByStance
import com.nicolas.models.player.enums.Stance
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.ROLL_RESULT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.STANCE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import com.nicolas.whfrp3companion.shared.getView

class ActionRollResultDialog : RollResultDialog() {
    private lateinit var actionEffectsListView: ListView

    private lateinit var action: Action
    private lateinit var stance: Stance

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action_roll_result, null, false)

        rollResult = arguments!!.getSerializable(ROLL_RESULT_INTENT_ARGUMENT) as RollResult
        action = arguments!!.getSerializable(ACTION_INTENT_ARGUMENT) as Action
        stance = arguments!!.getSerializable(STANCE_INTENT_ARGUMENT) as Stance

        builder.setView(view)
        builder.setTitle(R.string.results)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }

        val dialog = builder.create()
        bindViews(view)
        setResultsContent(rollResult)
        fillViews(action)

        return dialog
    }

    override fun bindViews(view: View) {
        super.bindViews(view)
        actionEffectsListView = view.getView(R.id.actionEffectsListView)
    }

    private fun fillViews(action: Action) {
        val (finalEffect, remainingReport, activatedEffects) = action.getRollResult(stance, rollResult)

        actionEffectsListView.adapter = ActionEffectsAdapter(context!!, action.getSideByStance(stance).effects!!, activatedEffects)
    }

    companion object {
        fun newInstance(rollResult: RollResult, action: Action, stance: Stance): ActionRollResultDialog {
            val args = Bundle()
            args.putSerializable(ROLL_RESULT_INTENT_ARGUMENT, rollResult)
            args.putSerializable(ACTION_INTENT_ARGUMENT, action)
            args.putSerializable(STANCE_INTENT_ARGUMENT, stance)

            val fragment = ActionRollResultDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
