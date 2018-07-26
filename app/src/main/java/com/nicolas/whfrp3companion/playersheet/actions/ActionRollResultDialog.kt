package com.nicolas.whfrp3companion.playersheet.actions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.nicolas.database.PlayerRepository
import com.nicolas.diceroller.roll.FacesReport
import com.nicolas.diceroller.roll.RollResult
import com.nicolas.diceroller.roll.getRollResult
import com.nicolas.models.action.Action
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.dice.Face
import com.nicolas.models.extensions.getActionEffectCritical
import com.nicolas.models.extensions.getActionEffectDamage
import com.nicolas.models.extensions.getEquippedWeaponsForCategories
import com.nicolas.models.extensions.getSideByStance
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.ROLL_RESULT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.RollResultDialog
import com.nicolas.whfrp3companion.shared.getView
import org.koin.android.ext.android.inject

class ActionRollResultDialog : RollResultDialog() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var damageTextView: TextView
    private lateinit var actionEffectsListView: ListView

    private lateinit var player: Player
    private lateinit var action: Action

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_action_roll_result, null, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        player = playerRepository.find(playerName)!!
        action = arguments!!.getSerializable(ACTION_INTENT_ARGUMENT) as Action
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
        val (finalEffect, remainingReport, activatedEffects) = action.getRollResult(player.currentStance, rollResult)

        actionEffectsListView.adapter = ActionEffectsAdapter(context!!, action.getSideByStance(player.currentStance).effects!!, activatedEffects)

        damageTextView.text = getString(R.string.action_damage_format)
                .format(getDamage(finalEffect, remainingReport), getCritical(finalEffect))
    }

    private fun getDamage(effect: ActionFaceEffect, remainingReport: FacesReport): Int =
            action.skill?.let {
                val overSuccess = when {
                    remainingReport[Face.SIGMAR] == null -> remainingReport[Face.SUCCESS]
                    else -> 0
                }

                player.getActionEffectDamage(it, effect, overSuccess, getUsedWeapon())
            } ?: 0

    private fun getCritical(effect: ActionFaceEffect): Int =
            getActionEffectCritical(effect, rollResult.report[Face.BOON], getUsedWeapon())

    private fun getUsedWeapon(): Weapon? {
        val actionWeaponCategories = action.conditions?.flatMap { it.weapon?.categories ?: listOf() } ?: listOf()
        val weapons = player.getEquippedWeaponsForCategories(actionWeaponCategories)

        return when {
            weapons.isNotEmpty() -> weapons[0]
            else -> null
        }
    }

    companion object {
        fun newInstance(rollResult: RollResult, action: Action, playerName: String): ActionRollResultDialog {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)
            args.putSerializable(ACTION_INTENT_ARGUMENT, action)
            args.putSerializable(ROLL_RESULT_INTENT_ARGUMENT, rollResult)

            val fragment = ActionRollResultDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
