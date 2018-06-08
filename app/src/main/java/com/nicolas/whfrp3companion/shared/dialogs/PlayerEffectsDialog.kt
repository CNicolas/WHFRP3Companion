package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.models.player.effect.Effect
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.state.PlayerEffectsActivity
import com.nicolas.whfrp3companion.shared.DIALOG_PLAYER_EFFECTS_TAG
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import org.jetbrains.anko.intentFor

class PlayerEffectsDialog : DialogFragment() {
    private lateinit var unbinder: Unbinder

    private lateinit var effects: List<Effect>

    @SuppressLint("InflateParams")
    @Suppress("UNCHECKED_CAST")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_roll_result, null, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        effects = (arguments!!.getSerializable(DIALOG_PLAYER_EFFECTS_TAG) as Array<Effect>).toList()

        builder.setView(view)
        builder.setTitle(R.string.page_effects)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.modify, { _, _ ->
            startActivity(view.context.intentFor<PlayerEffectsActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to playerName
            ))
        })

        val dialog = builder.create()
        unbinder = ButterKnife.bind(this, view)

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    companion object {
        fun newInstance(playerName: String, effects: List<Effect>): PlayerEffectsDialog {
            val args = Bundle()
            args.putSerializable(DIALOG_PLAYER_EFFECTS_TAG, effects.toTypedArray())
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerEffectsDialog()
            fragment.arguments = args

            return fragment
        }
    }
}