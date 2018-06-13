package com.nicolas.whfrp3companion.playersheet.inventory

import com.nicolas.models.item.Equipment
import com.nicolas.models.item.Item

interface ItemListener {
    fun onEquipment(equipment: Equipment, isEquipped: Boolean)
    fun onItemEditionDemand(item: Item)
    fun onItemDeleted(item: Item)
}
