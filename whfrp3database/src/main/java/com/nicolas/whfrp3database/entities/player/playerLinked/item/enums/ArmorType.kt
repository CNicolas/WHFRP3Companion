package com.nicolas.whfrp3database.entities.player.playerLinked.item.enums

import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorCategory.BODY
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorCategory.HEAD

enum class ArmorCategory {
    HEAD,
    BODY,
    SHIELD
}

enum class ArmorType(val category: ArmorCategory) {
    HELMET(HEAD),
    HAT(HEAD),

    PLATE(BODY),
    MAIL(BODY),
    LEATHER(BODY),
    ROBE(BODY),

    SHIELD(ArmorCategory.SHIELD);

    companion object {
        operator fun get(ordinal: Int): ArmorType = values()[ordinal]
    }
}