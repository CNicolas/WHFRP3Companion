package com.nicolas.models.item.enums

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