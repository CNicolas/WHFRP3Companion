package com.nicolas.whfrp3database.entities.player.playerLinked.item

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.ARMOR
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality

data class Armor(override var name: String = "Armor",
                 override var description: String? = null,
                 override var encumbrance: Int = 0,
                 override var quantity: Int = 1,
                 override var quality: Quality = Quality.NORMAL,

                 override var isEquipped: Boolean = false,

                 var subType: ArmorType = ArmorType.LEATHER,
                 var soak: Int = 0,
                 var defense: Int = 0) : Equipment {

    override var type = ARMOR
}