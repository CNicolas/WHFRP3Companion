package com.nicolas.whfrp3companion.playersheet.actions

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.nicolas.database.PlayerRepository
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionType
import com.nicolas.models.extensions.removeAction
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.advancedDiceRoller.PlayerAdvancedDiceRollerFragment
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.STANCE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.ActionDetailActivity
import com.nicolas.whfrp3companion.shared.adapters.ActionExpandableAdapter
import com.nicolas.whfrp3companion.shared.adapters.ActionListener
import com.nicolas.whfrp3companion.shared.getView
import kotlinx.android.synthetic.main.fragment_player_actions.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerActionsFragment : Fragment(), ActionListener {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_actions, container, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        player = playerRepository.find(playerName)!!

        setPlayerActionsAdapter()
        setupViewsEvents(resultingView)

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        setPlayerActionsAdapter()
    }

    override fun primaryHandler(action: Action) {
        activity?.let {
            it.startActivity(it.intentFor<ActionDetailActivity>(
                    ACTION_INTENT_ARGUMENT to action,
                    STANCE_INTENT_ARGUMENT to player.dominantStance
            ))
        }
    }

    override fun longPrimaryHandler(view: View, action: Action): Boolean {
        val actionPopupMenu = PopupMenu(view.context, view, Gravity.END)
        actionPopupMenu.menuInflater.inflate(R.menu.action_item, actionPopupMenu.menu)

        actionPopupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_action -> {
                    player.removeAction(action)
                    playerRepository.update(player)

                    setPlayerActionsAdapter()
                }
            }
            true
        }
        actionPopupMenu.show()

        return true
    }

    override fun secondaryHandler(action: Action) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.playersheet_content_frame, PlayerAdvancedDiceRollerFragment.newInstance(player.name, action))
                ?.commit()
    }

    private fun setPlayerActionsAdapter() {
        doAsync {
            player = playerRepository.find(player.name)!!

            uiThread { _ ->
                val expandedGroups = actions_expandable_list_view?.let { listView ->
                    listView.adapter?.let { _ ->
                        ActionType.values()
                                .map { listView.isGroupExpanded(it.ordinal) }
                    }
                }

                actions_expandable_list_view?.setAdapter(createActionsAdapter())

                expandedGroups?.forEachIndexed { groupIndex, expanded ->
                    if (expanded) {
                        actions_expandable_list_view.expandGroup(groupIndex)
                    }
                }
            }
        }
    }

    private fun setupViewsEvents(view: View) {
        view.getView<FloatingActionButton>(R.id.fab_add_action)
                .setOnClickListener { _ ->
                    startActivity(view.context.intentFor<PlayerAddActionsActivity>(
                            PLAYER_NAME_INTENT_ARGUMENT to player.name
                    ))
                }
    }

    // Keep 'this' as the context
    private fun createActionsAdapter(): ActionExpandableAdapter {
        return ActionExpandableAdapter(activity!!, player.actions, this, ActionExpandableAdapter.ActionButtonType.ROLL)
    }

    companion object {
        fun newInstance(playerName: String): PlayerActionsFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerActionsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}