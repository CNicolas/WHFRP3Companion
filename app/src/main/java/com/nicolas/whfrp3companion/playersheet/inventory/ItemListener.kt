package com.nicolas.whfrp3companion.playersheet.inventory

import com.nicolas.models.player.item.Equipment
import com.nicolas.models.player.item.Item

interface ItemListener {
    fun onEquipment(equipment: Equipment, isEquipped: Boolean)
    fun onItemEditionDemand(item: Item)
    fun onItemDeleted(item: Item)
}
