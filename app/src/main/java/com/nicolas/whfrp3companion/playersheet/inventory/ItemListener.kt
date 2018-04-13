package com.nicolas.whfrp3companion.playersheet.inventory

import com.nicolas.whfrp3database.entities.player.playerLinked.item.Item

interface ItemListener {
    fun onItemDeleted(item: Item)
    fun onItemEditionDemand(item: Item)
}

internal fun List<ItemListener>.notifyDeletion(item: Item) = forEach { it.onItemDeleted(item) }
internal fun List<ItemListener>.notifyEditionDemand(item: Item) = forEach { it.onItemEditionDemand(item) }