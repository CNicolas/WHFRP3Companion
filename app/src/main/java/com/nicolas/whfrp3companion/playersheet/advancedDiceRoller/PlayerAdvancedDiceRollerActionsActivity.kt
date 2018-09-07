package com.nicolas.whfrp3companion.playersheet.advancedDiceRoller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nicolas.database.PlayerRepository
import com.nicolas.models.action.Action
import com.nicolas.models.extensions.getEquippedWeapons
import com.nicolas.models.extensions.getEquippedWeaponsForCategories
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.DIALOG_ACTION_WEAPONS_TAG
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.WEAPON_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionExpandableAdapter
import com.nicolas.whfrp3companion.shared.adapters.ActionListener
import kotlinx.android.synthetic.main.activity_advanced_dice_roller_actions.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerAdvancedDiceRollerActionsActivity : AppCompatActivity(), ActionListener {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_dice_roller_actions)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var playerName: String? = null
        if (intent?.extras !== null) {
            playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        }

        playerName?.let {
            doAsync {
                player = playerRepository.find(it)!!

                uiThread {
                    setPlayerActionsAdapter()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()

        return true
    }

    override fun launchAction(action: Action) {
        val weaponCategories = action.conditions?.mapNotNull { it.weapon }?.flatMap { it.categories }
        val weapons = weaponCategories?.let {
            player.getEquippedWeaponsForCategories(it)
        } ?: player.getEquippedWeapons()

        if (weapons.isEmpty()) {
            return closeActivityAfterActionSelected(action)
        }

        ActionWeaponDialog.newInstance(weapons) { closeActivityAfterActionSelected(action, it) }
                .show(supportFragmentManager, DIALOG_ACTION_WEAPONS_TAG)
    }

    private fun closeActivityAfterActionSelected(action: Action, weapon: Weapon? = null) {
        val resultingIntent = Intent()
        resultingIntent.putExtra(ACTION_INTENT_ARGUMENT, action)
        resultingIntent.putExtra(WEAPON_INTENT_ARGUMENT, weapon)

        setResult(Activity.RESULT_OK, resultingIntent)
        finish()
    }

    private fun setPlayerActionsAdapter() {
        val adapter = ActionExpandableAdapter(this, player.actions, this, player.dominantStance)
        actions_expandable_list_view.setAdapter(adapter)

        (0 until player.actions.map { it.type }.distinct().size).forEach { it ->
            actions_expandable_list_view.expandGroup(it)
        }
    }
}