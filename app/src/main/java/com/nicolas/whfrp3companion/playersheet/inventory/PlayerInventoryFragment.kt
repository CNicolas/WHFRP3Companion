package com.nicolas.whfrp3companion.playersheet.inventory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.addMoney
import com.nicolas.models.extensions.getEquipmentByName
import com.nicolas.models.extensions.removeItem
import com.nicolas.models.extensions.removeMoney
import com.nicolas.models.item.Equipment
import com.nicolas.models.item.Item
import com.nicolas.models.item.enums.ItemType
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ITEM_EDIT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_player_inventory.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerInventoryFragment : Fragment(), ItemListener {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var playerName: String
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_inventory, container, false)

        playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        setupViews()
        setupViewsEvents()

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        setupViews()
    }

    // region ItemListener

    override fun onEquipment(equipment: Equipment, isEquipped: Boolean) {
        player.getEquipmentByName(equipment.name)?.isEquipped = isEquipped
        playerRepository.update(player)
    }

    override fun onItemEditionDemand(item: Item) {
        startItemEditionActivity(item)
    }

    override fun onItemDeleted(item: Item) {
        player.removeItem(item)
        playerRepository.update(player)

        setupViews()
    }

    // endregion

    private fun setupViewsEvents() {
        add_item.setOnClickListener { startItemEditionActivity() }
        change_money.setOnClickListener { changeMoney() }
    }

    private fun startItemEditionActivity(item: Item? = null) {
        activity?.let {
            startActivity(it.intentFor<ItemEditionActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name,
                    ITEM_EDIT_INTENT_ARGUMENT to item
            ))
        }
    }

    private fun setupViews() {
        val expandedGroups = inventory_list?.let { _ ->
            inventory_list.adapter?.let { _ ->
                ItemType.values().map { inventory_list.isGroupExpanded(it.ordinal) }
            }
        }

        doAsync {
            player = playerRepository.find(playerName)!!

            val inventoryAdapter = PlayerInventoryExpandableAdapter(context!!, player, this@PlayerInventoryFragment)

            uiThread {
                inventory_list.setAdapter(inventoryAdapter)
                expandedGroups?.forEachIndexed { groupIndex, expanded ->
                    if (expanded) {
                        inventory_list.expandGroup(groupIndex)
                    }
                }

                setupEncumbrance()
                setupMoney()
            }
        }
    }

    private fun setupEncumbrance() {
        encumbrance.min = 0
        encumbrance.max = player.maxEncumbrance
        encumbrance.progress = player.encumbrance
        encumbrance.isEnabled = false

        val colorId = when {
            player.encumbrance < player.encumbranceOverload -> R.color.conservative
            player.encumbrance < player.maxEncumbrance -> R.color.orange
            else -> R.color.reckless
        }
        val color = ContextCompat.getColor(context!!, colorId)
        val colorStateList = ColorStateList.valueOf(color)
        encumbrance.setScrubberColor(colorStateList)

        encumbrance_label.text = getString(R.string.value_on_max_value_format)
                .format(player.encumbrance, player.maxEncumbrance)
        encumbrance_label.setTextColor(colorStateList)
    }


    private fun setupMoney() {
        gold.text = "${player.gold}"
        silver.text = "${player.silver}"
        brass.text = "${player.brass}"
    }

    @SuppressLint("InflateParams")
    private fun changeMoney() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_money, null, false)

        val goldPicker = view.findViewById(R.id.gold) as NumberPicker
        val silverPicker = view.findViewById(R.id.silver) as NumberPicker
        val brassPicker = view.findViewById(R.id.brass) as NumberPicker

        builder.setView(view)
        builder.setTitle(R.string.change_money)

        builder.setNegativeButton(R.string.remove) { dialog, _ ->
            try {
                player.removeMoney(goldPicker.value, silverPicker.value, brassPicker.value)
                dialog.dismiss()
            } catch (exception: IllegalArgumentException) {
                context?.toast(R.string.not_enough_money)
            }
        }

        builder.setPositiveButton(R.string.add) { dialog, _ ->
            player.addMoney(goldPicker.value, silverPicker.value, brassPicker.value)
            dialog.dismiss()
        }

        builder.setOnDismissListener {
            doAsync {
                player = playerRepository.update(player)

                uiThread { _ ->
                    setupMoney()
                }
            }
        }

        builder.create().show()
    }

    companion object {
        fun newInstance(playerName: String): PlayerInventoryFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerInventoryFragment()
            fragment.arguments = args

            return fragment
        }
    }
}