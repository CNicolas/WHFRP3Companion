package com.nicolas.models.item.enums

enum class ItemType {
    GENERIC_ITEM,
    ARMOR,
    WEAPON,
    EXPANDABLE;

    companion object {
        operator fun get(ordinal: Int): ItemType = values()[ordinal]
    }
}