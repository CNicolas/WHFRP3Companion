package com.nicolas.whfrp3companion.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.dicelauncher.dices.Face
import com.nicolas.dicelauncher.launch.LaunchResult
import com.nicolas.whfrp3companion.R


class LaunchResultDialog() : DialogFragment() {
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

    private lateinit var unbinder: Unbinder

    private lateinit var launchResult: LaunchResult

    constructor(launchResult: LaunchResult) : this() {
        this.launchResult = launchResult
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_launch_result, null)

        unbinder = ButterKnife.bind(view)

        builder.setView(view)
        builder.setTitle(R.string.results)
        builder.setPositiveButton(android.R.string.ok, { _, _ -> dismiss() })

        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setResultsContent(launchResult)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    private fun setResultsContent(launchResult: LaunchResult) {
        when (launchResult.report[Face.SUCCESS]) {
            null -> successResult.visibility = GONE
            else -> successResult.text = launchResult.report[Face.SUCCESS].toString()
        }

        when (launchResult.report[Face.BOON]) {
            null -> boonResult.visibility = GONE
            else -> boonResult.text = launchResult.report[Face.BOON].toString()
        }

        when (launchResult.report[Face.SIGMAR]) {
            null -> sigmarResult.visibility = GONE
            else -> sigmarResult.text = launchResult.report[Face.SIGMAR].toString()
        }

        when (launchResult.report[Face.FAILURE]) {
            null -> failureResult.visibility = GONE
            else -> failureResult.text = launchResult.report[Face.FAILURE].toString()
        }

        when (launchResult.report[Face.BANE]) {
            null -> baneResult.visibility = GONE
            else -> baneResult.text = launchResult.report[Face.BANE].toString()
        }

        when (launchResult.report[Face.DELAY]) {
            null -> delayResult.visibility = GONE
            else -> delayResult.text = launchResult.report[Face.DELAY].toString()
        }

        when (launchResult.report[Face.EXHAUSTION]) {
            null -> exhaustionResult.visibility = GONE
            else -> exhaustionResult.text = launchResult.report[Face.EXHAUSTION].toString()
        }
    }
}