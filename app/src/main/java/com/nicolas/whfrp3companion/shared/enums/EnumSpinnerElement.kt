package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.*
import com.nicolas.whfrp3database.entities.player.enums.Race
import com.nicolas.whfrp3database.entities.player.enums.Race.*

internal val Race.labelId: Int
    get() = when (this) {
        HUMAN -> R.string.human
        DWARF -> R.string.dwarf
        HIGH_ELF -> R.string.high_elf
        WOOD_ELF -> R.string.wood_elf
    }

internal val Characteristic.labelId: Int
    get() = when (this) {
        STRENGTH -> R.string.strength
        TOUGHNESS -> R.string.toughness
        AGILITY -> R.string.agility
        INTELLIGENCE -> R.string.intelligence
        WILLPOWER -> R.string.willpower
        FELLOWSHIP -> R.string.fellowship
    }
