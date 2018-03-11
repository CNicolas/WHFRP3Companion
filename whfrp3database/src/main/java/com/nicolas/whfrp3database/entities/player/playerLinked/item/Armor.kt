package com.nicolas.whfrp3database.entities.player.playerLinked.item

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemSubType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.ARMOR
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range

data class Armor(override var name: String = "Armor",
                 override var description: String? = null,
                 override var encumbrance: Int = 0,
                 override var quantity: Int = 1,
                 override var quality: Quality = Quality.NORMAL,
                 override var subType: ItemSubType? = ArmorType.LEATHER,

                 override var isEquipped: Boolean? = false,

                 override var soak: Int? = 0,
                 override var defense: Int? = 0,

                 override val id: Int = -1) : Item {

    override var type = ARMOR

    override var uses: Int? = null
    override var damage: Int? = null
    override var criticalLevel: Int? = null
    override var range: Range? = null
}