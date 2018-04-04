package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View.GONE
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.roll.RollResult
import com.nicolas.whfrp3companion.R


class RollResultDialog() : DialogFragment() {
    @BindView(R.id.success_result)
    lateinit var successResult: TextView
    @BindView(R.id.boon_result)
    lateinit var boonResult: TextView
    @BindView(R.id.sigmar_result)
    lateinit var sigmarResult: TextView
    @BindView(R.id.failure_result)
    lateinit var failureResult: TextView
    @BindView(R.id.bane_result)
    lateinit var baneResult: TextView
    @BindView(R.id.delay_result)
    lateinit var delayResult: TextView
    @BindView(R.id.exhaustion_result)
    lateinit var exhaustionResult: TextView
    @BindView(R.id.chaos_result)
    lateinit var chaosResult: TextView

    private lateinit var unbinder: Unbinder

    private lateinit var rollResult: RollResult

    constructor(rollResult: RollResult) : this() {
        this.rollResult = rollResult
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_roll_result, null, false)

        builder.setView(view)
        builder.setTitle(R.string.results)
        builder.setPositiveButton(android.R.string.ok, { _, _ -> dismiss() })

        val dialog = builder.create()
        unbinder = ButterKnife.bind(this, view)

        setResultsContent()

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
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
}
