package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionType
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.drawableId
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.viewModifications.hide
import com.nicolas.whfrp3companion.shared.viewModifications.show

class ActionExpandableAdapter(context: Context,
                              private val actions: List<Action>,
                              private val actionListener: ActionListener? = null,
                              private val buttonType: ActionButtonType = ActionButtonType.NONE) : BaseExpandableListAdapter() {

    private val inflater = LayoutInflater.from(context)

    private val actionTypes: List<ActionType> = actions.map { it.type }.distinct()
    private val actionsByType: Map<ActionType, List<Action>> = actionTypes
            .map { type -> type to actions.filter { it.type == type } }
            .toMap()

    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getGroupViewHolderOfView(convertView, parent)

        holder.setupViews(getGroup(groupPosition), isExpanded)

        return resultingView
    }

    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getChildViewHolderOfView(convertView, parent)

        holder.setupViews(getChild(groupPosition, childPosition), buttonType)

        return resultingView
    }

    override fun getGroup(groupPosition: Int) = actionTypes[groupPosition]
    override fun getGroupCount() = actionTypes.size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChild(groupPosition: Int, childPosition: Int) = actionsByType[getGroup(groupPosition)]!![childPosition]
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int) = actionsByType[getGroup(groupPosition)]!!.size

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    private fun getGroupViewHolderOfView(savedView: View?, parent: ViewGroup?): Pair<View, GroupViewHolder> {
        var view = savedView

        val holder: GroupViewHolder
        if (view != null) {
            holder = view.tag as GroupViewHolder
        } else {
            view = inflater.inflate(R.layout.list_actions_group, parent, false)
            holder = GroupViewHolder(view)
            view!!.tag = holder
        }

        return Pair(view, holder)
    }

    private fun getChildViewHolderOfView(savedView: View?, parent: ViewGroup?): Pair<View, ChildViewHolder> {
        var view = savedView

        val holder: ChildViewHolder
        if (view != null) {
            holder = view.tag as ChildViewHolder
        } else {
            view = inflater.inflate(R.layout.list_actions_child, parent, false)
            holder = ChildViewHolder(view, actionListener)
            view!!.tag = holder
        }

        return Pair(view, holder)
    }

    internal class GroupViewHolder(view: View) {
        private val actionTypeImageView by view.bind<ImageView>(R.id.actionTypeImageView)
        private val actionTypeTextView by view.bind<TextView>(R.id.actionTypeTextView)
        private val expandImageView by view.bind<ImageView>(R.id.expandImageView)

        fun setupViews(actionType: ActionType, isExpanded: Boolean) {
            actionTypeImageView.setImageResource(actionType.drawableId)
            actionTypeTextView.setText(actionType.labelId)

            if (isExpanded) {
                expandImageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
            } else {
                expandImageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            }
        }
    }

    internal class ChildViewHolder(view: View,
                                   actionListener: ActionListener?) {
        private val actionChildLinearLayout by view.bind<LinearLayout>(R.id.action_child_linear_layout)
        private val actionNameTextView by view.bind<TextView>(R.id.action_name_text_view)
        private val actionAddButton by view.bind<ImageButton>(R.id.action_add_button)
        private val actionRollButton by view.bind<ImageButton>(R.id.action_roll_button)

        private lateinit var action: Action

        init {
            setupViewsEvents(actionListener)
        }

        fun setupViews(action: Action, buttonType: ActionButtonType) {
            this.action = action

            actionNameTextView.text = action.name

            when (buttonType) {
                ActionButtonType.NONE -> {
                    actionAddButton.hide()
                    actionRollButton.hide()
                }
                ActionButtonType.ADD -> {
                    actionAddButton.show()
                    actionRollButton.hide()
                }
                ActionButtonType.ROLL -> {
                    actionAddButton.hide()
                    actionRollButton.show()
                }
            }
        }

        private fun setupViewsEvents(actionListener: ActionListener?) {
            actionChildLinearLayout.setOnClickListener { actionListener?.primaryHandler(action) }
            actionChildLinearLayout.setOnLongClickListener { actionListener?.longPrimaryHandler(it, action) ?: true }
            actionAddButton.setOnClickListener { actionListener?.secondaryHandler(action) }
            actionRollButton.setOnClickListener { actionListener?.secondaryHandler(action) }
        }
    }

    enum class ActionButtonType {
        NONE,
        ADD,
        ROLL
    }
}