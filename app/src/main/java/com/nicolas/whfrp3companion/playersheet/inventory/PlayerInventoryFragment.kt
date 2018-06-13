package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.getEquipmentByName
import com.nicolas.models.extensions.removeItem
import com.nicolas.models.player.Player
import com.nicolas.models.player.item.Equipment
import com.nicolas.models.player.item.Item
import com.nicolas.models.player.item.enums.ItemType
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ITEM_EDIT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.fragment_player_inventory.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerInventoryFragment : Fragment(), ItemListener {
    private lateinit var unbinder: Unbinder

    private val playerRepository by inject<PlayerRepository>()

    private lateinit var playerName: String
    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_player_inventory, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        getPlayerItems()

        return resultingView
    }

    override fun onResume() {
        super.onResume()

        getPlayerItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.add_item)
    fun openAddItemActivity() {
        startItemEditionActivity()
    }

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

        getPlayerItems()
    }

    private fun startItemEditionActivity(item: Item? = null) {
        activity?.let {
            startActivity(it.intentFor<ItemEditionActivity>(
                    PLAYER_NAME_INTENT_ARGUMENT to player.name,
                    ITEM_EDIT_INTENT_ARGUMENT to item
            ))
        }
    }

    private fun getPlayerItems() {
        val expandedGroups = inventoryExpandableList?.let {
            it.adapter?.let {
                ItemType.values().map { inventoryExpandableList.isGroupExpanded(it.ordinal) }
            }
        }

        doAsync {
            player = playerRepository.find(playerName)!!

            val inventoryAdapter = PlayerInventoryExpandableAdapter(context!!, player, this@PlayerInventoryFragment)

            uiThread {
                inventoryExpandableList.setAdapter(inventoryAdapter)
                expandedGroups?.forEachIndexed { groupIndex, expanded ->
                    if (expanded) {
                        inventoryExpandableList.expandGroup(groupIndex)
                    }
                }
            }
        }
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