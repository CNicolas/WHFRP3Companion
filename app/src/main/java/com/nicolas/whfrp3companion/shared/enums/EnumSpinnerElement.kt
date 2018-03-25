package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.enums.Race

internal val Race.labelId: Int
    get() = when (this) {
        Race.HUMAN -> R.string.human
        Race.DWARF -> R.string.dwarf
        Race.HIGH_ELF -> R.string.high_elf
        Race.WOOD_ELF -> R.string.wood_elf
    }

internal val Characteristic.labelId: Int
    get() = when (this) {
        Characteristic.STRENGTH -> R.string.strength
        Characteristic.TOUGHNESS -> R.string.toughness
        Characteristic.AGILITY -> R.string.agility
        Characteristic.INTELLIGENCE -> R.string.intelligence
        Characteristic.WILLPOWER -> R.string.willpower
        Characteristic.FELLOWSHIP -> R.string.fellowship
    }
