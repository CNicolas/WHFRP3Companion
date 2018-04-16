package com.nicolas.whfrp3database.entities.player.playerLinked.item

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality

data class Expandable(override var name: String = "Expandable",
                      override var description: String? = null,
                      override var encumbrance: Int = 0,
                      override var quantity: Int = 1,
                      override var quality: Quality = Quality.NORMAL,

                      var uses: Int = 1) : Item {

    override var type = ItemType.EXPANDABLE
}