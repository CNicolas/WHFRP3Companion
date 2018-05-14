package com.nicolas.models.player.enums

enum class Race {
    HUMAN, DWARF, HIGH_ELF, WOOD_ELF;

    companion object {
        operator fun get(ordinal: Int): Race = values()[ordinal]
    }
}