package com.nicolas.whfrp3companion.fragments.actions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.loadActions
import com.nicolas.models.action.Action
import com.nicolas.whfrp3companion.R
import kotlinx.android.synthetic.main.fragment_player_actions.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ActionsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_actions, container, false)

        setActionsAdapter()

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        setActionsAdapter()
    }

    private fun setActionsAdapter() {
        doAsync {
            val actions = loadActions(context!!)

            uiThread {
                actionsRecyclerView?.let {
                    actionsRecyclerView.layoutManager = LinearLayoutManager(activity!!)
                    actionsRecyclerView.adapter = createActionsAdapter(actions)
                }
            }
        }
    }

    private fun createActionsAdapter(actions: List<Action>): ActionsAdapter {
        return ActionsAdapter(activity!!, actions)
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