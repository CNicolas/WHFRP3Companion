package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import com.nicolas.diceroller.roll.RollResult
import com.nicolas.models.dice.Face
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ROLL_RESULT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.getView

class RollResultDialog : DialogFragment() {
    lateinit var successResult: TextView
    lateinit var boonResult: TextView
    lateinit var sigmarResult: TextView
    lateinit var failureResult: TextView
    lateinit var baneResult: TextView
    lateinit var delayResult: TextView
    lateinit var exhaustionResult: TextView
    lateinit var chaosResult: TextView

    private lateinit var rollResult: RollResult

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_roll_result, null, false)

        rollResult = arguments!!.getSerializable(ROLL_RESULT_INTENT_ARGUMENT) as RollResult

        builder.setView(view)
        builder.setTitle(R.string.results)
        builder.setPositiveButton(android.R.string.ok, { _, _ -> dismiss() })

        val dialog = builder.create()
        bindViews(view)
        setResultsContent()

        return dialog
    }

    private fun setResultsContent() {
        when (rollResult.report[Face.SUCCESS]) {
            null -> successResult.visibility = GONE
            else -> successResult.text = rollResult.report[Face.SUCCESS].toString()
        }

        when (rollResult.report[Face.BOON]) {
            null -> boonResult.visibility = GONE
            else -> boonResult.text = rollResult.report[Face.BOON].toString()
        }

        when (rollResult.report[Face.SIGMAR]) {
            null -> sigmarResult.visibility = GONE
            else -> sigmarResult.text = rollResult.report[Face.SIGMAR].toString()
        }

        when (rollResult.report[Face.FAILURE]) {
            null -> failureResult.visibility = GONE
            else -> failureResult.text = rollResult.report[Face.FAILURE].toString()
        }

        when (rollResult.report[Face.BANE]) {
            null -> baneResult.visibility = GONE
            else -> baneResult.text = rollResult.report[Face.BANE].toString()
        }

        when (rollResult.report[Face.DELAY]) {
            null -> delayResult.visibility = GONE
            else -> delayResult.text = rollResult.report[Face.DELAY].toString()
        }

        when (rollResult.report[Face.EXHAUSTION]) {
            null -> exhaustionResult.visibility = GONE
            else -> exhaustionResult.text = rollResult.report[Face.EXHAUSTION].toString()
        }

        when (rollResult.report[Face.CHAOS]) {
            null -> chaosResult.visibility = GONE
            else -> chaosResult.text = rollResult.report[Face.CHAOS].toString()
        }
    }

    private fun bindViews(view: View) {
        successResult = view.getView(R.id.success_result)
        boonResult = view.getView(R.id.boon_result)
        sigmarResult = view.getView(R.id.sigmar_result)
        failureResult = view.getView(R.id.failure_result)
        baneResult = view.getView(R.id.bane_result)
        delayResult = view.getView(R.id.delay_result)
        exhaustionResult = view.getView(R.id.exhaustion_result)
        chaosResult = view.getView(R.id.chaos_result)
    }

    companion object {
        fun newInstance(rollResult: RollResult): RollResultDialog {
            val args = Bundle()
            args.putSerializable(ROLL_RESULT_INTENT_ARGUMENT, rollResult)

            val fragment = RollResultDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
