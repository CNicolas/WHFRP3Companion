package com.nicolas.whfrp3database.entities.player.playerLinked.item

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemSubType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range

data class Expandable(override var name: String = "Expandable",
                      override var description: String? = null,
                      override var encumbrance: Int = 0,
                      override var quantity: Int = 1,
                      override var quality: Quality = Quality.NORMAL,

                      override var uses: Int? = 0,

                      override val id: Int = -1) : Item {

    override var type = ItemType.EXPANDABLE

    override var subType: ItemSubType? = null
    override var isEquipped: Boolean? = null
    override var soak: Int? = null
    override var defense: Int? = null
    override var damage: Int? = null
    override var criticalLevel: Int? = null
    override var range: Range? = null
}