package com.nicolas.whfrp3database.entities.player.playerLinked.item

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.WEAPON
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.WeaponType

data class Weapon(override var name: String = "Weapon",
                  override var description: String? = null,
                  override var encumbrance: Int = 0,
                  override var quantity: Int = 1,
                  override var quality: Quality = Quality.NORMAL,

                  override var isEquipped: Boolean = false,

                  var subType: WeaponType = WeaponType.SWORD,
                  var damage: Int = 0,
                  var criticalLevel: Int = 0,
                  var range: Range = Range.ENGAGED) : Equipment {

    override var type = WEAPON
}