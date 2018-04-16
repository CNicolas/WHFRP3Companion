package com.nicolas.whfrp3database.entities.player.playerLinked.item.enums

enum class ItemType {
    ARMOR,
    EXPANDABLE,
    GENERIC_ITEM,
    WEAPON;

    companion object {
        operator fun get(ordinal: Int): ItemType = values()[ordinal]
    }
}