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

fun Range?.or(other: Range?): Range? {
    return when {
        other == null -> this
        this == null -> other
        else -> when {
            this.ordinal <= other.ordinal -> this
            else -> other
        }
    }
}