package com.nicolas.whfrp3database.entities.player.playerLinked.item

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemSubType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.WEAPON
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.WeaponType

data class Weapon(override var name: String = "Weapon",
                  override var description: String? = null,
                  override var encumbrance: Int = 0,
                  override var quantity: Int = 1,
                  override var quality: Quality = Quality.NORMAL,
                  override var subType: ItemSubType? = WeaponType.SWORD,

                  override var isEquipped: Boolean? = false,

                  override var damage: Int? = 0,
                  override var criticalLevel: Int? = 0,
                  override var range: Range? = Range.ENGAGED,

                  override val id: Int = -1) : Item {

    override var type = WEAPON

    override var uses: Int? = null
    override var soak: Int? = null
    override var defense: Int? = null
}