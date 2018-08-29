package com.nicolas.models.item.enums

enum class Quality {
    LOW,
    NORMAL,
    SUPERIOR,
    MAGIC;

    companion object {
        operator fun get(ordinal: Int): Quality = values()[ordinal]
    }
}