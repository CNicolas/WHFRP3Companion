package com.nicolas.models.player.playerLinked.item

import com.nicolas.models.player.playerLinked.item.enums.ItemType.GENERIC_ITEM
import com.nicolas.models.player.playerLinked.item.enums.Quality

data class GenericItem(override var name: String = "Item",
                       override var description: String? = null,
                       override var encumbrance: Int = 0,
                       override var quantity: Int = 1,
                       override var quality: Quality = Quality.NORMAL) : Item {

    override var type = GENERIC_ITEM
}