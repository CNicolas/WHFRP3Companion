package com.nicolas.models.action

enum class Trait {
    BASIC,
    DEFENSE,
    TEAMWORK,
    KILLER,
    CONTINUOUS,
    SWORD_WAY;

    companion object {
        operator fun get(ordinal: Int): Trait = Trait.values()[ordinal]
    }
}