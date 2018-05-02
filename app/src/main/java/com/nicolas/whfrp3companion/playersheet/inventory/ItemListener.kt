package com.nicolas.whfrp3companion.playersheet.inventory

import com.nicolas.models.player.playerLinked.item.Equipment
import com.nicolas.models.player.playerLinked.item.Item

interface ItemListener {
    fun onEquipment(equipment: Equipment, isEquipped: Boolean)
    fun onItemEditionDemand(item: Item)
    fun onItemDeleted(item: Item)
}

internal fun List<ItemListener>.notifyEquipment(equipment: Equipment, isEquipped: Boolean) = forEach { it.onEquipment(equipment, isEquipped) }
internal fun List<ItemListener>.notifyEditionDemand(item: Item) = forEach { it.onItemEditionDemand(item) }
internal fun List<ItemListener>.notifyDeletion(item: Item) = forEach { it.onItemDeleted(item) }
