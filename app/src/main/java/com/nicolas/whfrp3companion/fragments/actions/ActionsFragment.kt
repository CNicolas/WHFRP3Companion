package com.nicolas.whfrp3companion.fragments.actions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.nicolas.database.loadActions
import com.nicolas.models.action.Action
import com.nicolas.models.extensions.search
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_TALENT_SEARCH_TAG
import com.nicolas.whfrp3companion.shared.adapters.ActionExpandableAdapter
import com.nicolas.whfrp3companion.shared.dialogs.ActionSearchDialog
import kotlinx.android.synthetic.main.fragment_player_actions.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ActionsFragment : Fragment() {
    private var actions: List<Action> = listOf()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_actions, container, false)

        resetActions()

        search.setOnClickListener { openSearchDialog() }

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        setActionsAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.actions_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset_actions_filters -> resetActions()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSearchDialog() {
        activity?.let {
            ActionSearchDialog.newInstance { actionSearch ->
                doAsync {
                    actions = actions.search(actionSearch)

                    uiThread {
                        setActionsAdapter()
                    }
                }
            }.show(it.supportFragmentManager, ACTION_TALENT_SEARCH_TAG)
        }
    }

    private fun resetActions() {
        doAsync {
            actions = loadActions(context!!)

            uiThread {
                setActionsAdapter()
            }
        }
    }

    private fun setActionsAdapter() {
        activity?.let {
            actionsExpandableListView?.setAdapter(ActionExpandableAdapter(it, actions))
        }
    }

    companion object {
        fun newInstance(): ActionsFragment {
            val args = Bundle()
            val fragment = ActionsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}