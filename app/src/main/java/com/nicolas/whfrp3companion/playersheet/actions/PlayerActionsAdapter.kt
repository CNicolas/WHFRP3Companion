package com.nicolas.whfrp3companion.playersheet.actions

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.models.action.Action
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_ACTION_TAG
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.dialogs.ActionDialog

class PlayerActionsAdapter(context: Context,
                           private val actions: List<Action>,
                           private val actionListener: ActionListener) : RecyclerView.Adapter<PlayerActionsAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_element_action, parent, false)

        return ViewHolder(view, actionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupViews(actions[position])
    }

    override fun getItemCount(): Int = actions.size

    class ViewHolder(private val view: View,
                     private val actionListener: ActionListener) : RecyclerView.ViewHolder(view) {
        private val actionTypeImageView by view.bind<ImageView>(R.id.actionTypeImageView)
        private val actionNameTextView by view.bind<TextView>(R.id.actionNameTextView)
        private val actionCooldownTextView by view.bind<TextView>(R.id.actionCooldownTextView)

        private lateinit var action: Action

        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(action: Action) {
            this.action = action

            actionTypeImageView.setImageResource(R.drawable.ic_handgun_black)
            actionNameTextView.text = action.name
            actionCooldownTextView.text = action.conservativeSide.cooldown?.toString() ?: "0"
        }

        @OnClick(R.id.actionNameTextView)
        fun openActionInDialog() {
            val activity = view.context as? AppCompatActivity

            activity?.let {
                val actionDialog = ActionDialog.newInstance(action)
                actionDialog.show(activity.supportFragmentManager, DIALOG_ACTION_TAG)
            }
        }
    }
}