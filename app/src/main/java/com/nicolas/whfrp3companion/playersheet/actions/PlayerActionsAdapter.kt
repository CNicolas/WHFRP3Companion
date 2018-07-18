package com.nicolas.whfrp3companion.playersheet.actions

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.nicolas.models.action.Action
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_ACTION_TAG
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.dialogs.ActionDialog
import com.nicolas.whfrp3companion.shared.enums.drawableId

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
        private val actionMenuImageButton by view.bind<ImageButton>(R.id.actionMenuImageButton)

        private lateinit var action: Action

        fun setupViews(action: Action) {
            this.action = action

            actionTypeImageView.setImageResource(action.type.drawableId)
            actionNameTextView.text = action.name
            actionCooldownTextView.text = action.conservativeSide.cooldown?.toString() ?: "0"

            actionNameTextView.setOnClickListener { openActionInDialog() }
            actionMenuImageButton.setOnClickListener { openActionMenu(actionMenuImageButton) }
        }

        private fun openActionInDialog() {
            val activity = view.context as? AppCompatActivity

            activity?.let {
                val actionDialog = ActionDialog.newInstance(action)
                actionDialog.show(activity.supportFragmentManager, DIALOG_ACTION_TAG)
            }
        }

        private fun openActionMenu(view: View): Boolean {
            val itemPopupMenu = PopupMenu(view.context, view, Gravity.END)
            itemPopupMenu.menuInflater.inflate(R.menu.action, itemPopupMenu.menu)

            itemPopupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.launch_action -> {
                        actionListener.launchAction(action)
                    }
                }
                true
            }

            itemPopupMenu.show()

            return true
        }
    }
}