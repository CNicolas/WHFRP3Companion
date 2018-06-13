package com.nicolas.models.item

import com.nicolas.models.item.enums.ItemType
import com.nicolas.models.item.enums.Quality

data class Expandable(override var name: String = "Expandable",
                      override var description: String? = null,
                      override var encumbrance: Int = 0,
                      override var quantity: Int = 1,
                      override var quality: Quality = Quality.NORMAL,

                      var uses: Int = 1) : Item {

    override var type = ItemType.EXPANDABLE
}