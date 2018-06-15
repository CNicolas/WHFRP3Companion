package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.models.action.Action
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT

class ActionDialog : DialogFragment() {
    @BindView(R.id.actionNameTextView)
    lateinit var actionNameTextView: TextView

    private lateinit var unbinder: Unbinder

    private lateinit var action: Action

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action, null, false)

        action = arguments!!.getSerializable(ACTION_INTENT_ARGUMENT) as Action

        builder.setView(view)
        builder.setTitle(action.name)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }

        val dialog = builder.create()
        unbinder = ButterKnife.bind(this, view)

        setupViews()

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    private fun setupViews() {
        actionNameTextView.text = action.name
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
