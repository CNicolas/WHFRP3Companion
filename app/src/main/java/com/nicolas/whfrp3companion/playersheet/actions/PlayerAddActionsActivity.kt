package com.nicolas.whfrp3companion.playersheet.actions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nicolas.database.PlayerRepository
import com.nicolas.database.loadActions
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSearch
import com.nicolas.models.extensions.addAction
import com.nicolas.models.extensions.search
import com.nicolas.models.item.enums.ItemType
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.ACTION_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.ACTION_SEARCH_REQUEST_CODE
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.ActionDetailActivity
import com.nicolas.whfrp3companion.shared.activities.ActionSearchActivity
import com.nicolas.whfrp3companion.shared.adapters.ActionExpandableAdapter
import com.nicolas.whfrp3companion.shared.adapters.ActionListener
import kotlinx.android.synthetic.main.activity_player_add_actions.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerAddActionsActivity : AppCompatActivity(), ActionListener {

    private val playerRepository by inject<PlayerRepository>()

    private var initialActions: List<Action> = listOf()
    private var actions: List<Action> = listOf()
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_add_actions)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var playerName: String? = null
        if (intent?.extras !== null) {
            playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        }

        playerName?.let { name ->
            val activity = this

            doAsync {
                player = playerRepository.find(name)!!
                initialActions = loadActions(activity).filter { !player.actions.contains(it) }

                uiThread {
                    resetActions()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actions_page, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> openSearchActivity()
            R.id.reset_actions_filters -> resetActions()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTION_SEARCH_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                val actionSearch = data?.getSerializableExtra(ACTION_SEARCH_INTENT_ARGUMENT) as ActionSearch?
                actionSearch?.let {
                    doAsync {
                        actions = actions.search(actionSearch)

                        uiThread { _ ->
                            setActionsAdapter()
                        }
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun primaryHandler(action: Action) {
        startActivity(intentFor<ActionDetailActivity>(
                ACTION_INTENT_ARGUMENT to action
        ))
    }

    override fun secondaryHandler(action: Action) {
        doAsync {
            player.addAction(action)
            player = playerRepository.update(player)
            actions = actions.filter { it.name != action.name }

            uiThread {
                setActionsAdapter()
                longToast(getString(R.string.added_format).format(action.name))
            }
        }
    }

    private fun openSearchActivity() {
        startActivityForResult(intentFor<ActionSearchActivity>(), ACTION_SEARCH_REQUEST_CODE)
    }

    private fun resetActions() {
        actions = initialActions

        setActionsAdapter()
    }

    private fun setActionsAdapter() {
        val expandedGroups = actions_expandable_list_view?.let { listView ->
            listView.adapter?.let { _ ->
                ItemType.values()
                        .map { listView.isGroupExpanded(it.ordinal) }
            }
        }

        val adapter = ActionExpandableAdapter(this, actions, this, ActionExpandableAdapter.ActionButtonType.ADD)
        actions_expandable_list_view?.setAdapter(adapter)

        expandedGroups?.forEachIndexed { groupIndex, expanded ->
            if (expanded) {
                actions_expandable_list_view.expandGroup(groupIndex)
            }
        }
    }
}
