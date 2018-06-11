package com.nicolas.models.player.item

import com.nicolas.models.player.item.enums.ItemType
import com.nicolas.models.player.item.enums.Quality

data class Expandable(override var name: String = "Expandable",
                      override var description: String? = null,
                      override var encumbrance: Int = 0,
                      override var quantity: Int = 1,
                      override var quality: Quality = Quality.NORMAL,

                      var uses: Int = 1) : Item {

    override var type = ItemType.EXPANDABLE
}