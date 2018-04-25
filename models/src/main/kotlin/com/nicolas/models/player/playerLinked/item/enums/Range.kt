package com.nicolas.models.player.playerLinked.item.enums

enum class Range {
    ENGAGED,
    SHORT,
    MEDIUM,
    LONG,
    EXTREME;

    companion object {
        operator fun get(ordinal: Int): Range = values()[ordinal]
    }
}