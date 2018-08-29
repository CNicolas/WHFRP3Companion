package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionType
import com.nicolas.models.player.enums.Stance
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.STANCE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.ActionDetailActivity
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.drawableId
import com.nicolas.whfrp3companion.shared.enums.labelId
import org.jetbrains.anko.intentFor

class ActionExpandableAdapter(context: Context,
                              private val actions: List<Action>,
                              private val actionListener: ActionListener? = null,
                              private val dominantStance: Stance = Stance.CONSERVATIVE) : BaseExpandableListAdapter() {

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

        holder.setupViews(getChild(groupPosition, childPosition))

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
            holder = ChildViewHolder(view, actionListener, dominantStance)
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

    internal class ChildViewHolder(private val view: View,
                                   private val actionListener: ActionListener?,
                                   private val dominantStance: Stance) {
        private val actionChildLinearLayout by view.bind<LinearLayout>(R.id.actionChildLinearLayout)
        private val actionNameTextView by view.bind<TextView>(R.id.actionNameTextView)
        private val actionMenuImageButton by view.bind<ImageButton>(R.id.actionMenuImageButton)

        private lateinit var action: Action

        fun setupViews(action: Action) {
            this.action = action

            actionNameTextView.text = action.name

            actionListener?.let {
                val ta = view.context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
                actionChildLinearLayout.background = ta.getDrawable(0)
                ta.recycle()
            }

            setupViewsEvents(actionListener, action)
        }

        private fun setupViewsEvents(actionListener: ActionListener?, action: Action) {
            actionMenuImageButton.setOnClickListener { openActionInDialog() }

            actionListener?.let {
                actionChildLinearLayout.setOnClickListener { actionListener.launchAction(action) }
            }
        }

        private fun openActionInDialog() {
            val activity = view.context as? AppCompatActivity

            activity?.let {
                it.startActivity(it.intentFor<ActionDetailActivity>(
                        ACTION_INTENT_ARGUMENT to action,
                        STANCE_INTENT_ARGUMENT to dominantStance
                ))
            }
        }
    }
}