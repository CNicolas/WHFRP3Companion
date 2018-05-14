package com.nicolas.models.player.enums

enum class Characteristic {
    STRENGTH,
    TOUGHNESS,
    AGILITY,
    INTELLIGENCE,
    WILLPOWER,
    FELLOWSHIP;

    companion object {
        operator fun get(ordinal: Int): Characteristic = Characteristic.values()[ordinal]
    }
}