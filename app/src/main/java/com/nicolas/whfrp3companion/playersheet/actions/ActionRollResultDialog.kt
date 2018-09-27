package com.nicolas.whfrp3companion.playersheet.actions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.nicolas.database.PlayerRepository
import com.nicolas.diceroller.roll.RollResult
import com.nicolas.diceroller.roll.getRollResult
import com.nicolas.models.action.Action
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.dice.Face
import com.nicolas.models.extensions.getActionEffectCritical
import com.nicolas.models.extensions.getActionEffectDamage
import com.nicolas.models.extensions.getSideByStance
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.*
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import org.koin.android.ext.android.inject

class ActionRollResultDialog : RollResultDialog() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var damageTextView: TextView
    private lateinit var actionEffectsListView: ListView

    private lateinit var player: Player
    private lateinit var action: Action
    private var weapon: Weapon? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action_roll_result, null, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        player = playerRepository.find(playerName)!!
        action = arguments!!.getSerializable(ACTION_INTENT_ARGUMENT) as Action
        weapon = arguments!!.getSerializable(WEAPON_INTENT_ARGUMENT) as? Weapon
        rollResult = arguments!!.getSerializable(ROLL_RESULT_INTENT_ARGUMENT) as RollResult

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
        damageTextView = view.getView(R.id.damageTextView)
    }

    private fun fillViews(action: Action) {
        val (finalEffect, _, activatedEffects) = action.getRollResult(player.currentStance, rollResult)

        actionEffectsListView.adapter = ActionEffectsAdapter(context!!, action.getSideByStance(player.currentStance).effects!!, activatedEffects)

        val damageText = if (rollResult.isSuccessful) {
            getString(R.string.action_damage_format)
                    .format(getDamage(finalEffect), getCritical(finalEffect))
        } else {
            getString(R.string.failure)
        }
        damageTextView.text = damageText
    }

    private fun getDamage(effect: ActionFaceEffect): Int =
            action.skill?.let {
                player.getActionEffectDamage(it, effect, weapon)
            } ?: 0

    private fun getCritical(effect: ActionFaceEffect): Int =
            getActionEffectCritical(effect, rollResult.report[Face.BOON], weapon)

    companion object {
        fun newInstance(rollResult: RollResult, action: Action, playerName: String, weapon: Weapon?): ActionRollResultDialog {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)
            args.putSerializable(ACTION_INTENT_ARGUMENT, action)
            args.putSerializable(ROLL_RESULT_INTENT_ARGUMENT, rollResult)
            args.putSerializable(WEAPON_INTENT_ARGUMENT, weapon)

            val fragment = ActionRollResultDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
