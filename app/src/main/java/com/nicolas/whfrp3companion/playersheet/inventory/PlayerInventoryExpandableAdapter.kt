package com.nicolas.whfrp3companion.playersheet.inventory

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface.BOLD
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnLongClick
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.pluralLabelId
import com.nicolas.whfrp3companion.shared.hide
import com.nicolas.whfrp3companion.shared.show
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality.*
import com.nicolas.whfrp3database.extensions.getItemsOfType
import org.jetbrains.anko.longToast

class PlayerInventoryExpandableAdapter(private val context: Context,
                                       private val player: Player) : BaseExpandableListAdapter() {
    private val inflater = LayoutInflater.from(context)
    private val groupedItems = ItemType.values().map { it to player.getItemsOfType(it) }.toMap()

    private val itemListeners: MutableList<ItemListener> = mutableListOf()

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getGroupViewHolderOfView(convertView, parent)

        val itemType = getGroup(groupPosition)

        holder.itemTypeView.text = context.getString(itemType.pluralLabelId)

        return resultingView
    }

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        val (resultingView, holder) = getChildViewHolderOfView(convertView, parent)

        val item = getChild(groupPosition, childPosition)
        holder.setupViews(item)

        return resultingView
    }

    // region One line overrides
    override fun getGroup(groupPosition: Int) = ItemType.values()[groupPosition]

    override fun getGroupCount() = ItemType.values().size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChild(groupPosition: Int, childPosition: Int) = groupedItems[getGroup(groupPosition)]!![childPosition]
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int) = groupedItems[getGroup(groupPosition)]!!.size

    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
    // endregion

    fun addItemListener(itemListener: ItemListener) = itemListeners.add(itemListener)

    // region Group and Child ViewHolders creation
    private fun getGroupViewHolderOfView(savedView: View?, parent: ViewGroup?): Pair<View, GroupViewHolder> {
        var view = savedView

        val holder: GroupViewHolder
        if (view != null) {
            holder = view.tag as GroupViewHolder
        } else {
            view = inflater.inflate(R.layout.list_player_inventory_header, parent, false)
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
            view = inflater.inflate(R.layout.list_player_inventory_child, parent, false)
            holder = ChildViewHolder(itemListeners, view)
            view!!.tag = holder
        }

        return Pair(view, holder)
    }
    // endregion

    private class GroupViewHolder(view: View) {
        val itemTypeView by view.bind<TextView>(R.id.item_type)

        init {
            ButterKnife.bind(this, view)
        }
    }

    internal class ChildViewHolder(private val itemListeners: List<ItemListener>, view: View) {
        private val equippedImageView by view.bind<ImageView>(R.id.equipped)
        private val itemNameTextView by view.bind<TextView>(R.id.item_name)
        private val quantityTextView by view.bind<TextView>(R.id.quantity)

        private val defenseTextView by view.bind<TextView>(R.id.defense)
        private val soakTextView by view.bind<TextView>(R.id.soak)

        private val usesTextView by view.bind<TextView>(R.id.uses)

        private val damageTextView by view.bind<TextView>(R.id.damage)
        private val criticalLevelTextView by view.bind<TextView>(R.id.critical_level)
        private val rangeTextView by view.bind<TextView>(R.id.range)

        lateinit var item: Item

        private val armorViews
            get () = listOf(defenseTextView, soakTextView)
        private val expandableViews
            get() = listOf(usesTextView)
        private val weaponViews
            get () = listOf(damageTextView, criticalLevelTextView, rangeTextView)

        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(item: Item) {
            this.item = item

            itemNameTextView.text = item.name
            quantityTextView.text = item.quantity.toString()

            when (item.quality) {
                LOW -> itemNameTextView.setTextColor(ContextCompat.getColor(itemNameTextView.context, R.color.colorSecondaryText))
                NORMAL -> itemNameTextView.setTextColor(ContextCompat.getColor(itemNameTextView.context, R.color.colorPrimaryText))
                SUPERIOR -> itemNameTextView.setTextColor(ContextCompat.getColor(itemNameTextView.context, R.color.colorAccent))
                MAGIC -> itemNameTextView.setTextColor(ContextCompat.getColor(itemNameTextView.context, R.color.orange))
            }

            when (item.type) {
                ARMOR -> showArmorViews()
                EXPANDABLE -> showExpandableViews()
                WEAPON -> showWeaponViews()
                GENERIC_ITEM -> showGenericItemViews()
            }
        }

        @OnClick(R.id.item_name)
        fun selectItem(view: View) {
            view.context.longToast(item.toString())
        }

        @OnLongClick(R.id.item_name)
        fun openItemMenu(view: View): Boolean {
            val itemPopupMenu = PopupMenu(view.context, view, Gravity.END)
            if (item is Equipment) {
                if ((item as Equipment).isEquipped) {
                    itemPopupMenu.menuInflater.inflate(R.menu.inventory_equipment_equipped, itemPopupMenu.menu)
                } else {
                    itemPopupMenu.menuInflater.inflate(R.menu.inventory_equipment_unequipped, itemPopupMenu.menu)
                }
            } else {
                itemPopupMenu.menuInflater.inflate(R.menu.inventory_item, itemPopupMenu.menu)
            }

            itemPopupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.equip_item -> {
                        itemListeners.notifyEquipment(item as Equipment, true)
                        showEquippedImage(true)
                    }
                    R.id.unequip_item -> {
                        itemListeners.notifyEquipment(item as Equipment, false)
                        showEquippedImage(false)
                    }
                    R.id.edit_item -> {
                        itemListeners.notifyEditionDemand(item)
                    }
                    R.id.delete_item -> {
                        itemListeners.notifyDeletion(item)
                    }
                }
                true
            }

            itemPopupMenu.show()

            return true
        }

        private fun showArmorViews() {
            val armor = item as Armor

            defenseTextView.text = armor.defense.toString()
            soakTextView.text = armor.soak.toString()

            armorViews.show()
            expandableViews.hide()
            weaponViews.hide()

            showEquippedImage(armor.isEquipped)
        }

        private fun showExpandableViews() {
            val expandable = item as Expandable

            usesTextView.text = expandable.uses.toString()

            armorViews.hide()
            expandableViews.show()
            weaponViews.hide()
        }

        private fun showGenericItemViews() {
            armorViews.hide()
            expandableViews.hide()
            weaponViews.hide()
        }

        private fun showWeaponViews() {
            val weapon = item as Weapon

            damageTextView.text = weapon.damage.toString()
            criticalLevelTextView.text = weapon.criticalLevel.toString()
            rangeTextView.text = weapon.range.toString()

            armorViews.hide()
            expandableViews.hide()
            weaponViews.show()

            showEquippedImage(weapon.isEquipped)
        }

        private fun showEquippedImage(isEquipped: Boolean) = if (isEquipped) {
            itemNameTextView.setTypeface(null, BOLD)
            equippedImageView.visibility = VISIBLE
        } else {
            itemNameTextView.typeface = null
            equippedImageView.visibility = INVISIBLE
        }
    }
}