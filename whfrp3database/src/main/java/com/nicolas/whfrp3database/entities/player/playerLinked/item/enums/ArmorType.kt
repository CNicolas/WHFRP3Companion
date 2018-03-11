package com.nicolas.whfrp3database.entities.player.playerLinked.item.enums

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorCategory.BODY
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorCategory.HEAD

enum class ArmorType(override val category: ArmorCategory):ItemSubType {
    HELMET(HEAD),
    HAT(HEAD),

    PLATE(BODY),
    MAIL(BODY),
    LEATHER(BODY),
    ROBE(BODY),

    SHIELD(ArmorCategory.SHIELD),
    BULWARK(ArmorCategory.SHIELD)

}