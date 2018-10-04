package com.nicolas.whfrp3companion.fragments.actions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.*
import com.nicolas.database.loadActions
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSearch
import com.nicolas.models.extensions.search
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.ACTION_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.ACTION_SEARCH_REQUEST_CODE
import com.nicolas.whfrp3companion.shared.activities.ActionDetailActivity
import com.nicolas.whfrp3companion.shared.activities.ActionSearchActivity
import com.nicolas.whfrp3companion.shared.adapters.ActionExpandableAdapter
import com.nicolas.whfrp3companion.shared.adapters.ActionListener
import com.nicolas.whfrp3companion.shared.getView
import kotlinx.android.synthetic.main.fragment_actions.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread

class ActionsFragment : Fragment(), ActionListener {
    private var actions: List<Action> = listOf()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_actions, container, false)
        setHasOptionsMenu(true)

        resetActions()

        setupViewsEvents(resultingView)

        return resultingView
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
        activity?.let {
            it.startActivity(it.intentFor<ActionDetailActivity>(
                    ACTION_INTENT_ARGUMENT to action
            ))
        }
    }

    private fun setupViewsEvents(view: View) {
        view.getView<FloatingActionButton>(R.id.fab_search_action).setOnClickListener { openSearchActivity() }
    }

    private fun openSearchActivity() {
        activity?.let {
            it.startActivity(it.intentFor<ActionSearchActivity>())
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
            actions_expandable_list_view?.setAdapter(ActionExpandableAdapter(it, actions, this))
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