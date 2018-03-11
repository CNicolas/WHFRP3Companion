package com.nicolas.whfrp3database.entities.player.playerLinked.item.enums

interface ItemCategory

enum class ArmorCategory : ItemCategory {
    HEAD,
    BODY,
    SHIELD
}

enum class WeaponCategory : ItemCategory {
    MELEE,
    RANGE,
    FIRE_ARM,
    REPEATING
}

interface ItemSubType {
    val category: ItemCategory
}